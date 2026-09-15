package com.cadi.artedental.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.cadi.artedental.budget.entity.BudgetStatus;

public record BudgetResponse(

        UUID id,

        String patientId,

        String budgetNumber,

        BudgetStatus status,

        BigDecimal subtotal,

        BigDecimal discount,

        BigDecimal total,

        String notes,

        LocalDate validUntil,

        List<BudgetItemResponse> items,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}