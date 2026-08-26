package com.cadi.artedental.treatment.payment.controller;

import com.cadi.artedental.treatment.payment.dto.PatientFinancialSummaryResponse;
import com.cadi.artedental.treatment.payment.dto.TreatmentPaymentRequest;
import com.cadi.artedental.treatment.payment.dto.TreatmentPaymentResponse;
import com.cadi.artedental.treatment.payment.service.TreatmentPaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TreatmentPaymentController {

    private final TreatmentPaymentService paymentService;

    public TreatmentPaymentController(
        TreatmentPaymentService paymentService
    ) {
        this.paymentService = paymentService;
    }

    // =========================================================
    // REGISTRAR PAGO
    // =========================================================

    @PostMapping("/treatments/{treatmentId}/payments")
    public ResponseEntity<TreatmentPaymentResponse> registerPayment(
        @PathVariable String treatmentId,
        @RequestBody TreatmentPaymentRequest request
    ) {

        TreatmentPaymentResponse response =
            paymentService.registerPayment(
                treatmentId,
                request
            );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    // =========================================================
    // PAGOS POR TRATAMIENTO
    // =========================================================

    @GetMapping("/treatments/{treatmentId}/payments")
    public ResponseEntity<List<TreatmentPaymentResponse>>
        findByTreatmentId(
            @PathVariable String treatmentId
        ) {

        return ResponseEntity.ok(
            paymentService.findByTreatmentId(
                treatmentId
            )
        );
    }

    // =========================================================
    // PAGOS POR PACIENTE
    // =========================================================

    @GetMapping("/patients/{patientId}/payments")
    public ResponseEntity<List<TreatmentPaymentResponse>>
        findByPatientId(
            @PathVariable String patientId
        ) {

        return ResponseEntity.ok(
            paymentService.findByPatientId(
                patientId
            )
        );
    }

    // =========================================================
    // RESUMEN FINANCIERO
    // =========================================================

    @GetMapping("/patients/{patientId}/financial-summary")
    public ResponseEntity<PatientFinancialSummaryResponse>
        getFinancialSummary(
            @PathVariable String patientId
        ) {

        System.out.println(
            "GET financial-summary patientId = "
                + patientId
        );

        return ResponseEntity.ok(
            paymentService.getFinancialSummary(
                patientId
            )
        );
    }
}