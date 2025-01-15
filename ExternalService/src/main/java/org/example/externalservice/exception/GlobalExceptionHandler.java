package org.example.externalservice.exception;

import org.example.externalservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleRestClientException(RestClientException e) {
        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ApiResponse handleHttpClientErrorException(HttpClientErrorException e) {
        return new ApiResponse(e.getStatusCode().value(), e.getMessage());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ApiResponse handleHttpServerErrorException(HttpServerErrorException e) {
        return new ApiResponse(e.getStatusCode().value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleException(Exception e) {
        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
