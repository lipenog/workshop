package com.example.productproject.web.controller;

import com.example.productproject.web.dto.OrdersDTO;
import com.example.productproject.web.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {
    private final ProductsService productsService;

    @Autowired
    public OrdersController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody OrdersDTO ordersDTO){

    }
}
