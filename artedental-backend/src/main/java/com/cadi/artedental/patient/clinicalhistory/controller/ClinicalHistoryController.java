package com.cadi.artedental.patient.clinicalhistory.controller;
import com.cadi.artedental.patient.clinicalhistory.dto.request.SaveClinicalHistoryRequest;
import com.cadi.artedental.patient.clinicalhistory.dto.response.ClinicalHistoryResponse;
import com.cadi.artedental.patient.clinicalhistory.service.ClinicalHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    "/api/patients/{patientId}/clinical-history"
)
public class ClinicalHistoryController {

    private final ClinicalHistoryService service;

    public ClinicalHistoryController(
        ClinicalHistoryService service
    ) {
        this.service = service;
    }

    // =========================================================
    // GET
    // =========================================================

    @GetMapping
    public ClinicalHistoryResponse get(
        @PathVariable
        String patientId
    ) {
        return service.findByPatientId(
            patientId
        );
    }

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    @ResponseStatus(
        HttpStatus.CREATED
    )
    public ClinicalHistoryResponse create(
        @PathVariable
        String patientId,

        @Valid
        @RequestBody
        SaveClinicalHistoryRequest request
    ) {
        return service.create(
            patientId,
            request
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping
    public ClinicalHistoryResponse update(
        @PathVariable
        String patientId,

        @Valid
        @RequestBody
        SaveClinicalHistoryRequest request
    ) {
        return service.update(
            patientId,
            request
        );
    }
}