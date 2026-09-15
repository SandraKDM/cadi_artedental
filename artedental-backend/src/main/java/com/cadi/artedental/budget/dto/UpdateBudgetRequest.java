package com.cadi.artedental.budget.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UpdateBudgetRequest(

        LocalDate validUntil,

        @DecimalMin(value = "0.00") BigDecimal discount,

        @Size(max = 1000) String notes,

        @NotEmpty(message = "Debe existir al menos un tratamiento.") List<@Valid BudgetItemRequest> items

) {
}