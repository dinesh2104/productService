package com.example.product.controller;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.dtos.ProductWrapper;
import com.example.product.exception.InvalidProductIdException;
import com.example.product.models.Product;
import com.example.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    @Autowired
    @Qualifier("fakeStoreProductService")
    private IProductService productService;

    // Get all the products
    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    // Get a product with Id
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductWrapper> getSingleProduct(@PathVariable("id") Long id)  {

        ResponseEntity<ProductWrapper> response;
        try {
            Product singleProduct = productService.getSingleProduct(id);
            ProductWrapper productWrapper = new ProductWrapper(singleProduct, "Successfully retried the data");
            response = new ResponseEntity<>(productWrapper, HttpStatus.OK);
        } catch (InvalidProductIdException e) {
            ProductWrapper productWrapper = new ProductWrapper(null, "Product is not present ");
            response =  new ResponseEntity<>(productWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody ProductRequestDto productRequestDto){
        return productService.addProduct(productRequestDto.getTitle(),productRequestDto.getDescription(),productRequestDto.getPrice(),productRequestDto.getImage(),productRequestDto.getCategory(),productRequestDto.getEmail());
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable("id") Long id,
                                 @RequestBody ProductRequestDto productRequestDto){
        return productService.updateProduct(id, productRequestDto);
    }

    @DeleteMapping("/products/{id}")
    public boolean deleteProduct(@PathVariable("id") Long id){
        return true;
    }


}
