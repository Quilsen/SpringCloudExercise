package org.example.externalservice.dto;

import java.math.BigDecimal;

public record ProductCreateDto(String name,
                               String description,
                               BigDecimal price) {
}
