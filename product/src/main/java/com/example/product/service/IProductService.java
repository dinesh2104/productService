package com.example.product.service;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.exception.InvalidProductIdException;
import com.example.product.models.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();

    Product getSingleProduct(Long id) throws InvalidProductIdException;

    Product updateProduct(Long id, ProductRequestDto productRequestDto);

    Product addProduct(String title, String description, int price, String image, String category,String email);
}
