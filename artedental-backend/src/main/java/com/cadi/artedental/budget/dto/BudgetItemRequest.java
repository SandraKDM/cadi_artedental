package com.cadi.artedental.budget.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetItemRequest(

        @NotNull(message = "El elemento del catálogo es obligatorio") UUID catalogItemId,

        @Size(max = 10, message = "El número de diente no puede superar 10 caracteres") String toothNumber,

        @Min(value = 1, message = "La cantidad debe ser al menos 1") Integer quantity,

        @DecimalMin(value = "0.00", message = "El descuento no puede ser negativo") BigDecimal discount,

        @Size(max = 500, message = "Las notas no pueden superar 500 caracteres") String notes,

        @Size(max = 500, message = "La descripción no puede superar 500 caracteres") String description

) {
}