package org.example.internalservice.exception;

import org.example.internalservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleProductNotFoundException(ProductNotFoundException e) {
        return new ApiResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
}
