package org.example.externalservice.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ProductDto(Long id,
                         OffsetDateTime createdOn,
                         OffsetDateTime updatedOn,
                         String name,
                         String description,
                         BigDecimal price) {
}
