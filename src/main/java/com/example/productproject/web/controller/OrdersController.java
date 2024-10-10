package com.example.productproject.web.controller;

import com.example.productproject.exception.InvalidDtoException;
import com.example.productproject.exception.InvalidProductException;
import com.example.productproject.web.dto.OrdersDTO;
import com.example.productproject.web.entity.Orders;
import com.example.productproject.web.service.OrdersService;
import com.stripe.exception.StripeException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class OrdersController {
    private final OrdersService ordersService;

    @Autowired
    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody OrdersDTO ordersDTO) throws InvalidDtoException, InvalidProductException, StripeException {
        List<String> violations = verifyDTO(ordersDTO);
        if(!violations.isEmpty()){
            throw new InvalidDtoException(violations);
        }
        return ResponseEntity.ok(ordersService.createSessionCheckout(ordersDTO));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrdersDTO>> getAllOrders(){
        List<Orders> orders = ordersService.getAllOrders();
        return ResponseEntity.ok(orders.stream().map(OrdersDTO::new).collect(Collectors.toList()));
    }

    private List<String> verifyDTO(OrdersDTO ordersDTO){
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
            Set<ConstraintViolation<OrdersDTO>> violations = validator.validate(ordersDTO);
            return violations.stream().map(ConstraintViolation::getMessage).toList();
        }
    }
}
