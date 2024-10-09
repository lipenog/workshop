package com.example.productproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@AllArgsConstructor
@Getter
public class InvalidDtoException extends Exception {
    private final List<String> errorMessages;
}
