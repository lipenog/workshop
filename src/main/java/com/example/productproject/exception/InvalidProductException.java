package com.example.productproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class InvalidProductException extends RuntimeException{
    private Long productID;
    private String errorMessage;
}
