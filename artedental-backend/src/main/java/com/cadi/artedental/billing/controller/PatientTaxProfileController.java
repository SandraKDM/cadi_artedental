package com.cadi.artedental.billing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cadi.artedental.billing.dto.PatientTaxProfileRequest;
import com.cadi.artedental.billing.dto.PatientTaxProfileResponse;
import com.cadi.artedental.billing.service.PatientTaxProfileService;

@RestController
@RequestMapping(
    "/api/patients/{patientId}/tax-profile"
)
public class PatientTaxProfileController {

    private final PatientTaxProfileService
        taxProfileService;

    public PatientTaxProfileController(
        PatientTaxProfileService taxProfileService
    ) {
        this.taxProfileService =
            taxProfileService;
    }

    // =========================================================
    // GET TAX PROFILE
    // =========================================================

    @GetMapping
    public ResponseEntity<PatientTaxProfileResponse>
        getTaxProfile(
            @PathVariable String patientId
        ) {

        return ResponseEntity.ok(
            taxProfileService
                .getByPatientId(
                    patientId
                )
        );
    }

    // =========================================================
    // CREATE / UPDATE TAX PROFILE
    // =========================================================

    @PutMapping
    public ResponseEntity<PatientTaxProfileResponse>
        saveTaxProfile(
            @PathVariable String patientId,
            @RequestBody
                PatientTaxProfileRequest request
        ) {

        return ResponseEntity.ok(
            taxProfileService.save(
                patientId,
                request
            )
        );
    }
}