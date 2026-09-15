package com.cadi.artedental.billing.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cadi.artedental.billing.dto.InvoiceCandidateResponse;
import com.cadi.artedental.billing.service.InvoiceCandidateService;

@RestController
@RequestMapping("/api/patients/{patientId}/invoice-candidates")
public class InvoiceCandidateController {

    private final InvoiceCandidateService invoiceCandidateService;

    public InvoiceCandidateController(
            InvoiceCandidateService invoiceCandidateService) {
        this.invoiceCandidateService = invoiceCandidateService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceCandidateResponse>> getCandidates(
            @PathVariable String patientId) {

        System.out.println("CONTROLLER -> GET invoice-candidates: " + patientId);

        return ResponseEntity.ok(
                invoiceCandidateService
                        .getCandidates(
                                patientId));
    }
}