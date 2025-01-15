package org.example.internalservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.internalservice.dto.ProductCreateDto;
import org.example.internalservice.dto.ProductDto;
import org.example.internalservice.dto.ProductUpdateDto;
import org.example.internalservice.exception.ProductNotFoundException;
import org.example.internalservice.model.Product;
import org.example.internalservice.repository.ProductRepository;
import org.example.internalservice.repository.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class ProductService {
    private static final String PRODUCT_NOT_FOUND_MSG = "Product not found with id: ";
    private final ProductRepository productRepository;
    private ObjectMapper objectMapper;

    public ProductDto createProduct(ProductCreateDto productCreateDto) {
        Product product = objectMapper.convertValue(productCreateDto, Product.class);
        Product save = productRepository.save(product);
        return objectMapper.convertValue(save, ProductDto.class);
    }

    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException( PRODUCT_NOT_FOUND_MSG + id));
        return objectMapper.convertValue(product, ProductDto.class);
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> objectMapper.convertValue(product, ProductDto.class))
                .toList();
    }

    public ProductDto updateProduct(Long id, ProductUpdateDto productUpdateDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id));

        if (productUpdateDto.description() != null) {
            product.setDescription(productUpdateDto.description());
        }
        if (productUpdateDto.price() != null) {
            product.setPrice(productUpdateDto.price());
        }

        Product save = productRepository.save(product);
        return objectMapper.convertValue(save, ProductDto.class);
    }

    public Page<ProductDto> filterProducts(Pageable pageable, String name, String description, BigDecimal price) {
        Specification<Product> spec = ProductSpecification.buidlSpec(name, description, price);

        return productRepository.findAll(spec, pageable)
                .map(product -> objectMapper.convertValue(product, ProductDto.class));
    }


        public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id);
        }
        productRepository.deleteById(id);
    }
}
