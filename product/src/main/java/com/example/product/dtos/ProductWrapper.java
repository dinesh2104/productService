package com.example.product.dtos;

import com.example.product.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWrapper {
    private Product prod;
    private String message;

    public ProductWrapper(Product prod, String message) {
        this.prod = prod;
        this.message = message;
    }
}
