package org.example.externalservice.service;

import lombok.AllArgsConstructor;
import org.example.externalservice.dto.ProductCreateDto;
import org.example.externalservice.dto.ProductDto;
import org.example.externalservice.dto.ProductUpdateDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private static final String PRODUCTS_URL = "http://internal-service/api/products";
    private RestTemplate restTemplate;

    public ProductDto createProduct(ProductCreateDto productCreateDto) {
        ResponseEntity<ProductDto> exchange = restTemplate.exchange(
                PRODUCTS_URL,
                HttpMethod.POST,
                new HttpEntity<>(productCreateDto),
                new ParameterizedTypeReference<ProductDto>() {
                }
        );
        return exchange.getBody();
    }

    public ProductDto getProduct(Long id) {
        ResponseEntity<ProductDto> exchange = restTemplate.exchange(
                PRODUCTS_URL + "/" + id,
                HttpMethod.GET,
                null,
                ProductDto.class
        );
        return exchange.getBody();
    }

    public List<ProductDto> getAllProducts() {
        ResponseEntity<List<ProductDto>> exchange = restTemplate.exchange(
                PRODUCTS_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductDto>>() {
                }
        );
        return exchange.getBody();
    }

    public ProductDto updateProduct(Long id, ProductUpdateDto productUpdateDto) {
        ResponseEntity<ProductDto> exchange = restTemplate.exchange(
                PRODUCTS_URL + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(productUpdateDto),
                ProductDto.class
        );
        return exchange.getBody();
    }

    public void deleteProduct(Long id) {
        restTemplate.delete(PRODUCTS_URL + "/" + id);
    }

}
