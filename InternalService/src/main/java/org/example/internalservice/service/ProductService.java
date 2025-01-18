package org.example.internalservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@AllArgsConstructor
@Service
public class ProductService {
    private static final String PRODUCT_NOT_FOUND_MSG = "Product not found with id: ";
    private final ProductRepository productRepository;
    private ObjectMapper objectMapper;

    public ProductDto createProduct(ProductCreateDto productCreateDto) {
        log.info("Creating product: {}", productCreateDto);
        Product product = objectMapper.convertValue(productCreateDto, Product.class);
        Product save = productRepository.save(product);
        log.info("Product saved: {}", product);
        return objectMapper.convertValue(save, ProductDto.class);
    }

    public ProductDto getProduct(Long id) {
        log.info("Getting product: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id));
        log.info("Product found with ID : {}", product.getId());
        return objectMapper.convertValue(product, ProductDto.class);
    }

    public List<ProductDto> getAllProducts() {
        log.info("Getting all products");
        List<ProductDto> productDtos = productRepository.findAll()
                .stream()
                .map(product -> objectMapper.convertValue(product, ProductDto.class))
                .toList();
        log.info("Retrieved {} products", productDtos.size());
        return productDtos;
    }

    public ProductDto updateProduct(Long id, ProductUpdateDto productUpdateDto) {
        log.info("Updating product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id));

        if (productUpdateDto.description() != null) {
            log.debug("Updating description for product ID: {}", id);
            product.setDescription(productUpdateDto.description());
        }
        if (productUpdateDto.price() != null) {
            log.debug("Updating price for product ID: {}", id);
            product.setPrice(productUpdateDto.price());
        }

        Product save = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", id);
        return objectMapper.convertValue(save, ProductDto.class);
    }

    public Page<ProductDto> filterProducts(Pageable pageable, String name, String description, BigDecimal price) {
        log.info("Filtering products with parameters - Name: {}, Description: {}, Price: {}", name, description, price);
        Specification<Product> spec = ProductSpecification.buidlSpec(name, description, price);

        Page<ProductDto> productDtos = productRepository.findAll(spec, pageable)
                .map(product -> objectMapper.convertValue(product, ProductDto.class));
        log.info("Filtered products: {} total elements", productDtos.getTotalElements());
        return productDtos;
    }


    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND_MSG + id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted successfully with ID: {}", id);
    }
}
