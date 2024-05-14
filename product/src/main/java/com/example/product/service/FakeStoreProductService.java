package com.example.product.service;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.dtos.ProductResponseDto;
import com.example.product.exception.InvalidProductIdException;
import com.example.product.models.Category;
import com.example.product.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Qualifier("fakeStoreProductService")
public class FakeStoreProductService implements IProductService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String,Long> redisTemplate;

    public Product getProductFromResponseDto(ProductResponseDto productResponseDto) {
        Product p=new Product();
        p.setId(productResponseDto.getId());
        p.setName(productResponseDto.getTitle());
        p.setPrice(productResponseDto.getPrice());
        p.setImage(productResponseDto.getImage());
        p.setDescription(productResponseDto.getDescription());
        p.setCategory(new Category());

        p.getCategory().setName(productResponseDto.getCategory());
        return p;
    }

    @Override
    public List<Product> getAllProducts() {
        ProductResponseDto[] result = restTemplate.getForObject("https://fakestoreapi.com/products", ProductResponseDto[].class);

        List<Product> products = new ArrayList<>();
        for(ProductResponseDto prod:result){
            products.add(getProductFromResponseDto(prod));
        }
        return products;
    }

    @Override
    public Product getSingleProduct(Long id) throws InvalidProductIdException {
        if(redisTemplate.opsForHash().hasKey("PRODUCT",id)){
            return (Product) redisTemplate.opsForHash().get("PRODUCT",id);
        }

        if(id>10){
            throw new InvalidProductIdException("Invalid Id");
        }
        ProductResponseDto productResponseDto = restTemplate.getForObject("https://fakestoreapi.com/products/"+id, ProductResponseDto.class);
        Product product = getProductFromResponseDto(productResponseDto);
        redisTemplate.opsForHash().put("PRODUCT",id,product);
        System.out.println("result:"+redisTemplate.expire("PRODUCT",5, TimeUnit.SECONDS));
        return product;
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDto productRequestDto) {
        RequestCallback requestCallback = restTemplate.httpEntityCallback(productRequestDto, ProductResponseDto.class);
        HttpMessageConverterExtractor<ProductResponseDto> responseExtractor =
                new HttpMessageConverterExtractor<>(ProductResponseDto.class,
                        restTemplate.getMessageConverters());
        ProductResponseDto responseDto = restTemplate.execute("https://fakestoreapi.com/products/" + id,
                HttpMethod.PUT, requestCallback, responseExtractor);
        return getProductFromResponseDto(responseDto);
    }

    @Override
    public Product addProduct(String title, String description, int price, String image, String category,String email) {
        return null;
    }
}
