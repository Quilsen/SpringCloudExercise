package org.example.internalservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.internalservice.dto.ProductCreateDto;
import org.example.internalservice.dto.ProductDto;
import org.example.internalservice.dto.ProductUpdateDto;
import org.example.internalservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    private final Long PRODUCT_ID = 1L;
    private final String NAME = "Test Product";
    private final String DESCRIPTION = "Test Description";
    private final BigDecimal PRICE = BigDecimal.valueOf(9.99);

    @Test
    void createProduct_ReturnsCreatedProduct() throws Exception {
        ProductCreateDto createDto = new ProductCreateDto(NAME, DESCRIPTION, PRICE);
        ProductDto productDto = new ProductDto(PRODUCT_ID, null, null, NAME, DESCRIPTION, PRICE);

        given(productService.createProduct(any(ProductCreateDto.class))).willReturn(productDto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.price").value(PRICE.doubleValue()));
    }

    @Test
    void getProduct_ReturnsProduct() throws Exception {
        ProductDto productDto = new ProductDto(PRODUCT_ID, null, null, NAME, DESCRIPTION, PRICE);
        given(productService.getProduct(PRODUCT_ID)).willReturn(productDto);

        mockMvc.perform(get("/api/products/{id}", PRODUCT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.description").value(DESCRIPTION))
                .andExpect(jsonPath("$.price").value(PRICE.doubleValue()));
    }

    @Test
    void getAllProducts_ReturnsListOfProducts() throws Exception {
        ProductDto productDto1 = new ProductDto(1L, null, null, "Product1", "Desc1", PRICE);
        ProductDto productDto2 = new ProductDto(2L, null, null, "Product2", "Desc2", PRICE);
        List<ProductDto> products = List.of(productDto1, productDto2);

        given(productService.getAllProducts()).willReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(products.size()))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void filterProducts_ReturnsFilteredProductsPage() throws Exception {
        ProductDto productDto = new ProductDto(PRODUCT_ID, null, null, NAME, DESCRIPTION, PRICE);
        Pageable pageable = PageRequest.of(0, 10);
        given(productService.filterProducts(any(Pageable.class), eq(NAME), eq(DESCRIPTION), eq(PRICE)))
                .willReturn(new PageImpl<>(List.of(productDto), pageable, 1));

        mockMvc.perform(get("/api/products/filter")
                        .param("name", NAME)
                        .param("description", DESCRIPTION)
                        .param("price", PRICE.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(PRODUCT_ID));
    }

    @Test
    void updateProduct_ReturnsUpdatedProduct() throws Exception {
        ProductUpdateDto updateDto = new ProductUpdateDto("Updated description", PRICE);
        ProductDto updatedDto = new ProductDto(PRODUCT_ID, null, null, NAME, "Updated description", PRICE);

        given(productService.updateProduct(eq(PRODUCT_ID), any(ProductUpdateDto.class))).willReturn(updatedDto);

        mockMvc.perform(put("/api/products/{id}", PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.price").value(PRICE.doubleValue()));
    }

    @Test
    void deleteProduct_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", PRODUCT_ID))
                .andExpect(status().isNoContent());
    }
}
