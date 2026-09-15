package com.cadi.artedental.budget.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateBudgetRequest(

        LocalDate validUntil,

        BigDecimal discount,

        @Size(max = 1000, message = "Las notas no pueden superar 1000 caracteres") String notes,

        @NotEmpty(message = "El presupuesto debe contener al menos un tratamiento") List<@Valid BudgetItemRequest> items

) {
}