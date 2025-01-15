package org.example.externalservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.example.externalservice.dto.ProductCreateDto;
import org.example.externalservice.dto.ProductDto;

import org.example.externalservice.dto.ProductUpdateDto;
import org.example.externalservice.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductsController {
    private ProductService productService;

    @Operation(summary = "Create a new product", description = "Creates a new product based on the provided details.")
    @ApiResponse(responseCode = "201", description = "Product successfully created")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody ProductCreateDto productCreateDto) {
        return productService.createProduct(productCreateDto);
    }

    @Operation(summary = "Get product by ID", description = "Retrieves details of a product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @Operation(summary = "Get all products", description = "Retrieves a list of all available products.")
    @ApiResponse(responseCode = "200", description = "List of products retrieved successfully")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(
            summary = "Filter products",
            description = "Retrieve a paginated list of products based on filters such as name, description, and price. Supports pagination and sorting.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of products"),
                    @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductDto> filterProducts(
            @Parameter(description = "Name of the product to filter", example = "Laptop")
            @RequestParam(required = false) String name,

            @Parameter(description = "Description of the product to filter", example = "Gaming laptop")
            @RequestParam(required = false) String description,

            @Parameter(description = "Price of the product to filter", example = "1000.00")
            @RequestParam(required = false) BigDecimal price,

            @Parameter(description = "Page number for pagination", example = "0")
            @RequestParam(required = false) Integer page,

            @Parameter(description = "Page size for pagination", example = "10")
            @RequestParam(required = false) Integer size,

            @Parameter(description = "Sorting criteria in the format 'field,order' (e.g., 'name,asc')", example = "name,asc")
            @RequestParam(required = false) String sort
    ) {
        return productService.filterProducts(page, size, sort, name, description, price);
    }

    @Operation(summary = "Update product by ID", description = "Updates an existing product with the provided details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDto productUpdateDto) {
        return productService.updateProduct(id, productUpdateDto);
    }

    @Operation(summary = "Delete product by ID", description = "Deletes an existing product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
