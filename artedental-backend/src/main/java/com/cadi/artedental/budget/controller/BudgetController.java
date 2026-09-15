package com.cadi.artedental.budget.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cadi.artedental.budget.dto.BudgetResponse;
import com.cadi.artedental.budget.dto.CreateBudgetRequest;
import com.cadi.artedental.budget.dto.UpdateBudgetRequest;
import com.cadi.artedental.budget.dto.UpdateBudgetStatusRequest;
import com.cadi.artedental.budget.service.BudgetService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients/{patientId}/budgets")
public class BudgetController {

    private final BudgetService service;

    public BudgetController(
            BudgetService service) {
        this.service = service;
    }

    // =========================================================
    // CREAR PRESUPUESTO
    // =========================================================

    @PostMapping
    public ResponseEntity<BudgetResponse> create(
            @PathVariable String patientId,
            @Valid @RequestBody CreateBudgetRequest request) {

        BudgetResponse response = service.create(
                patientId,
                request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // OBTENER PRESUPUESTOS DEL PACIENTE
    // =========================================================

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> findAll(
            @PathVariable String patientId) {

        List<BudgetResponse> response = service.findByPatientId(patientId);

        return ResponseEntity.ok(response);
    }

    // =========================================================
    // OBTENER PRESUPUESTO POR ID
    // =========================================================

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> findById(
            @PathVariable String patientId,
            @PathVariable UUID budgetId) {

        BudgetResponse response = service.findById(
                patientId,
                budgetId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> update(

            @PathVariable String patientId,

            @PathVariable UUID budgetId,

            @Valid @RequestBody UpdateBudgetRequest request) {

        return ResponseEntity.ok(
                service.update(
                        patientId,
                        budgetId,
                        request));
    }

    @PatchMapping("/{budgetId}/status")
    public ResponseEntity<BudgetResponse> updateStatus(

            @PathVariable String patientId,

            @PathVariable UUID budgetId,

            @Valid @RequestBody UpdateBudgetStatusRequest request) {

        return ResponseEntity.ok(
                service.updateStatus(
                        patientId,
                        budgetId,
                        request.status()));
    }
}