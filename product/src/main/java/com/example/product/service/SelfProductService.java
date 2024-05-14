package com.example.product.service;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.dtos.UserResponseDto;
import com.example.product.exception.InvalidProductIdException;
import com.example.product.models.Category;
import com.example.product.models.Product;
import com.example.product.repository.CategoryRepo;
import com.example.product.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("selfProductService")
public class SelfProductService implements IProductService{

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product getSingleProduct(Long id) throws InvalidProductIdException {
        return null;
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDto productRequestDto) {
        return null;
    }

    @Override
    public Product addProduct(String title, String description, int price, String image, String category,String email) {
        String req="{\"email\":\""+email+"\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request=new HttpEntity<>(req,headers);
        ResponseEntity<String> user = restTemplate.exchange("http://userservice/user",HttpMethod.POST, request, String.class);
        System.out.println(user.getBody());
        if(user.getStatusCode()!= HttpStatus.OK){
            throw new RuntimeException("Invalid User");
        }
        Product p=new Product();
        p.setName(title);
        p.setDescription(description);
        p.setPrice(price);
        p.setImage(image);

        Optional<Category> optionalCategory = categoryRepo.findByName(category);
        if(optionalCategory.isEmpty()){
            Category c=new Category();
            c.setName(category);
            categoryRepo.save(c);
            p.setCategory(c);
        }else{
            p.setCategory(optionalCategory.get());
        }
        return productRepo.save(p);
    }
}
