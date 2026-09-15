package com.cadi.artedental.budget.dto;

import com.cadi.artedental.budget.entity.BudgetStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateBudgetStatusRequest(

        @NotNull
        BudgetStatus status

) {}