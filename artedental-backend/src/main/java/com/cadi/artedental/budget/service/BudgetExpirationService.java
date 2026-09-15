package com.cadi.artedental.budget.service;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadi.artedental.budget.repository.BudgetRepository;

@Service
public class BudgetExpirationService {

    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Mexico_City");

    private final BudgetRepository budgetRepository;

    public BudgetExpirationService(
            BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public int expireBudgets() {

        LocalDate today = LocalDate.now(
                BUSINESS_ZONE);

        int updated = budgetRepository.expireBudgets(today);

        if (updated > 0) {
            System.out.println(
                    "Presupuestos expirados actualizados: "
                            + updated);
        }

        return updated;
    }
}