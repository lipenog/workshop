package com.example.productproject.web.service;

import com.example.productproject.web.dto.Product;
import com.example.productproject.web.entity.Products;
import com.example.productproject.web.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Products createProduct(Product product){
        Products productsEntity = new Products(product);
        return productsRepository.save(productsEntity);
    }
}
