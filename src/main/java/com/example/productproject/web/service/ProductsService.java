package com.example.productproject.web.service;

import com.example.productproject.web.dto.ProductsDTO;
import com.example.productproject.web.entity.Products;
import com.example.productproject.web.repository.ProductsRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.param.ProductCreateParams;
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

    public Products saveProduct(ProductsDTO productsDTO) throws StripeException {
        Products productsEntity = new Products(productsDTO);
        Product StripeProduct = Product.create(ProductCreateParams.builder()
                .setName(productsEntity.getName())
                .setDescription(productsEntity.getDescription())
                        .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                                .setCurrency("brl")
                                .setUnitAmount((long) (productsEntity.getPrice() * 100)).build())
                .build());
        StripeProduct.getId();
        return productsRepository.save(productsEntity);
    }
    
    public List<Products> getAllProducts(){
        return productsRepository.findAll();
    }
    
    public Optional<Products> getProductByID(Long id){
        return productsRepository.findById(id);
    }

    public void deleteProduct(Long id){
        productsRepository.deleteById(id);
    }
}
