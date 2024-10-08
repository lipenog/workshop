package com.example.productproject.web.controller;

import com.example.productproject.web.dto.ProductDTO;
import com.example.productproject.web.entity.Products;
import com.example.productproject.web.service.ProductsService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ProductsController {
    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> productPost(@RequestBody ProductDTO productDTO){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(productDTO);
        if(!violations.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Products products = productsService.createProduct(productDTO);
        ProductDTO response = new ProductDTO(products);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
