package com.cadi.artedental.budget.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cadi.artedental.budget.service.BudgetExpirationService;

@Component
public class BudgetExpirationScheduler {

    private final BudgetExpirationService budgetExpirationService;

    public BudgetExpirationScheduler(
            BudgetExpirationService budgetExpirationService) {
        this.budgetExpirationService = budgetExpirationService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Mexico_City")
    public void updateExpiredBudgets() {

        budgetExpirationService.expireBudgets();
    }
}