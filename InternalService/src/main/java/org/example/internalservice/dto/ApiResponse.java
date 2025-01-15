package org.example.internalservice.dto;

import java.time.LocalDateTime;

public record ApiResponse(
        int statusCode,
        String message,
        LocalDateTime timestamp
) {
    public ApiResponse(int statusCode, String message) {
        this(statusCode, message, LocalDateTime.now());
    }

    public record ExceptionMessage(String errorMessage) {}
}