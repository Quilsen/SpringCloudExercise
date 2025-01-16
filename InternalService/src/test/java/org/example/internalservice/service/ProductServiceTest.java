package org.example.internalservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.internalservice.dto.ProductCreateDto;
import org.example.internalservice.dto.ProductDto;
import org.example.internalservice.dto.ProductUpdateDto;
import org.example.internalservice.exception.ProductNotFoundException;
import org.example.internalservice.model.Product;
import org.example.internalservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductService productService;

    private final Long PRODUCT_ID = 1L;
    private final String NAME = "Mocked Product Name";
    private final String DESCRIPTION = "Mocked Product Description";
    private final BigDecimal PRICE = BigDecimal.valueOf(9.99);

    @Test
    void createProduct_ShouldReturnProductDto_WhenProductIsCreated() {
        // given
        ProductCreateDto productCreateDto = new ProductCreateDto(NAME, DESCRIPTION, PRICE);
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(NAME);
        product.setDescription(DESCRIPTION);
        product.setPrice(PRICE);

        ProductDto productDto = new ProductDto(PRODUCT_ID, null, null, NAME, DESCRIPTION, PRICE);

        when(objectMapper.convertValue(productCreateDto, Product.class)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(objectMapper.convertValue(product, ProductDto.class)).thenReturn(productDto);

        // when
        ProductDto result = productService.createProduct(productCreateDto);

        // then
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.id());
        assertEquals(NAME, result.name());
        assertEquals(DESCRIPTION, result.description());
        assertEquals(PRICE, result.price());

        verify(objectMapper).convertValue(productCreateDto, Product.class);
        verify(productRepository).save(product);
        verify(objectMapper).convertValue(product, ProductDto.class);
    }

    @Test
    void getProduct_ShouldReturnProductDto_WhenProductExists() {
        // given
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(NAME);
        product.setDescription(DESCRIPTION);
        product.setPrice(PRICE);

        ProductDto productDto = new ProductDto(PRODUCT_ID, null, null, NAME, DESCRIPTION, PRICE);

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(objectMapper.convertValue(product, ProductDto.class)).thenReturn(productDto);

        // when
        ProductDto result = productService.getProduct(PRODUCT_ID);

        // then
        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.id());
        verify(productRepository).findById(PRODUCT_ID);
        verify(objectMapper).convertValue(product, ProductDto.class);
    }

    @Test
    void getProduct_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        // given
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        // when / then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProduct(PRODUCT_ID));
        assertTrue(exception.getMessage().contains("Product not found with id: " + PRODUCT_ID));
        verify(productRepository).findById(PRODUCT_ID);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProductDtos() {
        // given
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("P1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("P2");

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));

        ProductDto productDto1 = new ProductDto(1L, null, null, "P1", null, null);
        ProductDto productDto2 = new ProductDto(2L, null, null, "P2", null, null);

        when(objectMapper.convertValue(product1, ProductDto.class)).thenReturn(productDto1);
        when(objectMapper.convertValue(product2, ProductDto.class)).thenReturn(productDto2);

        // when
        List<ProductDto> result = productService.getAllProducts();

        // then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
        verify(productRepository).findAll();
        verify(objectMapper, times(2)).convertValue(any(Product.class), eq(ProductDto.class));
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnUpdatedProductDto_WhenProductExists() {
        // given
        Long id = 10L;
        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setDescription("Old description");
        existingProduct.setPrice(BigDecimal.valueOf(1.00));

        ProductUpdateDto productUpdateDto = new ProductUpdateDto("New description", BigDecimal.valueOf(2.00));

        Product updatedProduct = new Product();
        updatedProduct.setId(id);
        updatedProduct.setDescription("New description");
        updatedProduct.setPrice(BigDecimal.valueOf(2.00));

        ProductDto updatedProductDto = new ProductDto(id, null, null, null, "New description", BigDecimal.valueOf(2.00));

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(objectMapper.convertValue(updatedProduct, ProductDto.class)).thenReturn(updatedProductDto);

        // when
        ProductDto result = productService.updateProduct(id, productUpdateDto);

        // then
        assertEquals("New description", result.description());
        assertEquals(BigDecimal.valueOf(2.00), result.price());
        verify(productRepository).findById(id);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        // given
        Long id = 10L;
        ProductUpdateDto productUpdateDto = new ProductUpdateDto("New description", BigDecimal.valueOf(2.00));
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(id, productUpdateDto));
        assertTrue(exception.getMessage().contains("Product not found with id: " + id));
        verify(productRepository).findById(id);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void filterProducts_ShouldReturnFilteredPageOfProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName(NAME);
        product.setDescription(DESCRIPTION);
        product.setPrice(PRICE);

        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll((Specification<Product>) any(), eq(pageable))).thenReturn(productPage);

        ProductDto productDto = new ProductDto(PRODUCT_ID, null, null, NAME, DESCRIPTION, PRICE);
        when(objectMapper.convertValue(product, ProductDto.class)).thenReturn(productDto);

        // when
        Page<ProductDto> result = productService.filterProducts(pageable, NAME, DESCRIPTION, PRICE);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(PRODUCT_ID, result.getContent().get(0).id());
        verify(productRepository).findAll((Specification<Product>) any(), eq(pageable));
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        // given
        when(productRepository.existsById(PRODUCT_ID)).thenReturn(true);

        // when
        productService.deleteProduct(PRODUCT_ID);

        // then
        verify(productRepository).existsById(PRODUCT_ID);
        verify(productRepository).deleteById(PRODUCT_ID);
    }

    @Test
    void deleteProduct_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        // given
        when(productRepository.existsById(PRODUCT_ID)).thenReturn(false);

        // when / then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(PRODUCT_ID));
        assertTrue(exception.getMessage().contains("Product not found with id: " + PRODUCT_ID));
        verify(productRepository).existsById(PRODUCT_ID);
        verify(productRepository, never()).deleteById(PRODUCT_ID);
    }
}
