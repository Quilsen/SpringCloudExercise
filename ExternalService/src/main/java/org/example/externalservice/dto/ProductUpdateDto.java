package org.example.externalservice.dto;

import java.math.BigDecimal;

public record ProductUpdateDto(String description,
                               BigDecimal price) {
}
