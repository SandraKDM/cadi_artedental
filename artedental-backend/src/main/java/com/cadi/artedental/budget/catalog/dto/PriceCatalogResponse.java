package com.cadi.artedental.budget.catalog.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PriceCatalogResponse(

    UUID id,

    String code,

    String name,

    String description,

    String category,

    BigDecimal price,

    Boolean requiresTooth,

    Boolean active,

    LocalDateTime createdAt,

    LocalDateTime updatedAt

) {
}