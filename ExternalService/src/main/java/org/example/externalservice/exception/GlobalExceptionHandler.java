package org.example.externalservice.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.externalservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

@Log4j2
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ObjectMapper objectMapper;

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ApiResponse> handleRestClientException(RestClientException e) {
        ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiResponse> handleHttpClientErrorException(HttpClientErrorException e) {
        return handleHttpException(e);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ApiResponse> handleHttpServerErrorException(HttpServerErrorException e) {
        return handleHttpException(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception e) {
        ApiResponse response = new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ResponseEntity<ApiResponse> handleHttpException(HttpStatusCodeException e) {
        try {
            String body = e.getResponseBodyAsString();
            if (body != null && !body.isEmpty()) {
                ApiResponse apiResponse = objectMapper.readValue(body, ApiResponse.class);
                return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
            }
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse error response: {}", ex.getMessage(), ex);
        }
        ApiResponse apiResponse = new ApiResponse(e.getStatusCode().value(), e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
    }
}
