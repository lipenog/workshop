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
        Product StripeProduct = creteStripeProduct(productsEntity.getName(), productsEntity.getDescription(), productsEntity.getPrice());

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

        updateStripeProduct(productsEntity.getStripeID(), productsEntity.getName(), productsEntity.getDescription(), productsEntity.getPrice());

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

    private Product creteStripeProduct(String name, String description, Float price) throws StripeException {
        return Product.create(ProductCreateParams.builder()
                .setName(name)
                .setDescription(description)
                .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                        .setCurrency("brl")
                        .setUnitAmount((long) (price * 100)).build())
                .build());
    }

    private void updateStripeProduct(String productID, String name, String description, Float productPrice) throws StripeException {
        Product product = Product.retrieve(productID);
        Price oldPrice = Price.retrieve(product.getDefaultPrice());

        ProductUpdateParams.Builder params = ProductUpdateParams.builder();

        // set the new name and price
        params.setName(name);
        params.setDescription(description);

        // if the price hasn't changed update just the product
        if(oldPrice.getUnitAmount().equals((long) (productPrice * 100))){
            product.update(params.build());
            return;
        }

        // create a new price to that product (Since stripe does not let you update the price)
        Price price = Price.create(PriceCreateParams.builder()
                .setCurrency("brl")
                .setUnitAmount((long) (productPrice * 100))
                .setProduct(productID)
                .build());

        params.setDefaultPrice(price.getId());

        // update stripe product
        Product resource = product.update(params.build());

        // set the old price as inactive
        oldPrice.update(PriceUpdateParams.builder().setActive(false).build());
    }
}
