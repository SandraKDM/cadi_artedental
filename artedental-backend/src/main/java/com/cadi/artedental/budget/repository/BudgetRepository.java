package com.cadi.artedental.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cadi.artedental.budget.entity.Budget;
import com.cadi.artedental.budget.entity.BudgetStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BudgetRepository
        extends JpaRepository<Budget, UUID> {

    List<Budget> findByPatientIdOrderByCreatedAtDesc(
            String patientId);

    List<Budget> findByPatientIdAndStatusOrderByCreatedAtDesc(
            String patientId,
            BudgetStatus status);

    Optional<Budget> findByIdAndPatientId(
            UUID id,
            String patientId);

    Optional<Budget> findByBudgetNumber(
            String budgetNumber);

    boolean existsByBudgetNumber(
            String budgetNumber);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            UPDATE budgets
            SET
                status = 'EXPIRED',
                updated_at = CURRENT_TIMESTAMP
            WHERE valid_until IS NOT NULL
              AND valid_until < :today
              AND status IN ('DRAFT', 'PENDING')
            """, nativeQuery = true)
    int expireBudgets(
            @Param("today") LocalDate today);
}