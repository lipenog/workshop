package com.example.productproject.handler;

import com.example.productproject.exception.InvalidDtoException;
import com.example.productproject.web.controller.ProductsController;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    private final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InvalidDtoException.class)
    public ResponseEntity<Void> handleDTOException(InvalidDtoException ex){
        String message = formatErrorMessage(ex.getErrorMessages());
        System.out.println(message);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleGenericException(Exception ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String formatErrorMessage(List<String> errorMessages){
        Locale currentLocale = LocaleContextHolder.getLocale();
        System.out.println(currentLocale);
        return errorMessages.stream()
                .map(e -> formatErrorMessage(e, currentLocale))
                .collect(Collectors.joining("\n"))
                .trim();
    }

    private String formatErrorMessage(String errorMessage, Locale locale){
        return messageSource.getMessage(errorMessage, null, locale);
    }
}
