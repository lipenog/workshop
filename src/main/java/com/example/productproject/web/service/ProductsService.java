package com.example.productproject.web.service;

import com.example.productproject.web.dto.ProductsDTO;
import com.example.productproject.web.entity.Products;
import com.example.productproject.web.repository.ProductsRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceUpdateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductUpdateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Products createProduct(ProductsDTO productsDTO) throws StripeException {
        Products productsEntity = new Products(productsDTO);

        // create the product inside stripe
        Product StripeProduct = Product.create(ProductCreateParams.builder()
                .setName(productsEntity.getName())
                .setDescription(productsEntity.getDescription())
                        .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                                .setCurrency("brl")
                                .setUnitAmount((long) (productsEntity.getPrice() * 100)).build())
                .build());

        productsEntity.setId(null);
        productsEntity.setStripeID(StripeProduct.getId());

        return productsRepository.save(productsEntity);
    }

    public Products updateProduct(ProductsDTO productsDTO, Products original) throws StripeException {
        Products productsEntity = new Products(productsDTO);

        // copy original product
        productsEntity.setId(original.getId());
        productsEntity.setCreation(original.getCreation());
        productsEntity.setStripeID(original.getStripeID());

        Product product = Product.retrieve(productsEntity.getStripeID());
        Price oldPrice = Price.retrieve(product.getDefaultPrice());

        // create a new price to that product (Since stripe does not let you update the price)
        Price price = Price.create(PriceCreateParams.builder()
                .setCurrency("brl")
                .setUnitAmount((long) (productsEntity.getPrice() * 100))
                .setProduct(productsEntity.getStripeID())
                .build());

        // update stripe product
        product.update(ProductUpdateParams.builder()
                .setName(productsEntity.getName())
                .setDescription(productsEntity.getDescription())
                .setDefaultPrice(price.getId())
                .build());

        // set the old price as inactive
        oldPrice.update(PriceUpdateParams.builder().setActive(false).build());

        return productsRepository.save(productsEntity);
    }
    
    public List<Products> getAllProducts(){
        return productsRepository.findAll();
    }
    
    public Optional<Products> getProductByID(Long id){
        return productsRepository.findById(id);
    }

    public void deleteProduct(Products products) throws StripeException {
        // Stripe does not support product deletion, so we are just setting the product as inactive and deleted
        Product product = Product.retrieve(products.getStripeID());
        product.update(ProductUpdateParams.builder().setActive(false).build());
        productsRepository.delete(products);
    }
}
