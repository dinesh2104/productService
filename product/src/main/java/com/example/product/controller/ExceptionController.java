package com.example.product.controller;


import com.example.product.dtos.ProductWrapper;
import com.example.product.exception.InvalidProductIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<ProductWrapper> handleInvalidProductIdException(InvalidProductIdException e){

        ProductWrapper productWrapper = new ProductWrapper(null, "Product is not present ");
        return  new ResponseEntity<>(productWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
