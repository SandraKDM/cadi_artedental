package com.cadi.artedental.budget.dto;
import java.math.BigDecimal;
import java.util.UUID;

public record BudgetItemResponse(

    UUID id,

    UUID catalogItemId,

    String catalogCode,

    String treatmentName,

    String description,

    String toothNumber,

    BigDecimal unitPrice

) {
}