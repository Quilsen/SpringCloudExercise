package org.example.externalservice.service;

import lombok.AllArgsConstructor;
import org.example.externalservice.dto.CustomPageImpl;
import org.example.externalservice.dto.ProductCreateDto;
import org.example.externalservice.dto.ProductDto;
import org.example.externalservice.dto.ProductUpdateDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
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

    public Page<ProductDto> filterProducts(Integer page, Integer size, String sort, String name, String description,  BigDecimal price) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(PRODUCTS_URL + "/filter");
        if (page != null) {
            uri.queryParam("page", page);
        }
        if (size != null) {
            uri.queryParam("size", size);
        }
        if (StringUtils.hasText(sort)) {
            uri.queryParam("sort", sort);
        }
        if (StringUtils.hasText(name)) {
            uri.queryParam("name", name);
        }
        if (StringUtils.hasText(description)) {
            uri.queryParam("description", description);
        }
        if (price != null) {
            uri.queryParam("price", price);
        }

        ResponseEntity<CustomPageImpl<ProductDto>> exchange = restTemplate.exchange(
                uri.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CustomPageImpl<ProductDto>>() {}
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
