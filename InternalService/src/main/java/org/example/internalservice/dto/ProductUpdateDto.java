package org.example.internalservice.dto;

import java.math.BigDecimal;

public record ProductUpdateDto(String description,
                               BigDecimal price) {
}
