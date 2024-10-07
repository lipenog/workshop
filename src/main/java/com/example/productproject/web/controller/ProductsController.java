package com.example.productproject.web.controller;

import com.example.productproject.web.dto.Product;
import com.example.productproject.web.entity.Products;
import com.example.productproject.web.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductsController {
    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping("/products")
    public ResponseEntity<Products> productPost(@RequestBody Product product){
        Products products = productsService.createProduct(product);
        return new ResponseEntity<>(products, HttpStatus.CREATED);
    }
}
