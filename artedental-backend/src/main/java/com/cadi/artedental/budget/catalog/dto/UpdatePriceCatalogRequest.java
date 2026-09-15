package com.cadi.artedental.budget.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdatePriceCatalogRequest(

        @NotBlank(message = "El nombre es obligatorio") @Size(max = 150, message = "El nombre no puede superar 150 caracteres") String name,

        @Size(max = 500, message = "La descripción no puede superar 500 caracteres") String description,

        @NotBlank(message = "La categoría es obligatoria") @Size(max = 80, message = "La categoría no puede superar 80 caracteres") String category,

        @NotNull(message = "El precio es obligatorio") @DecimalMin(value = "0.00", inclusive = true, message = "El precio no puede ser negativo") BigDecimal price,

        Boolean requiresTooth,

        @NotNull(message = "El estado activo es obligatorio") Boolean active

) {
}