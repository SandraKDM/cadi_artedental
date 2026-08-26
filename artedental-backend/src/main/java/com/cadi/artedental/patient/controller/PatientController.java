package com.cadi.artedental.patient.controller;

import com.cadi.artedental.patient.dto.request.CreatePatientRequest;
import com.cadi.artedental.patient.dto.request.UpdatePatientRequest;
import com.cadi.artedental.patient.dto.response.PatientResponse;
import com.cadi.artedental.patient.service.PatientService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(
        PatientService service
    ) {
        this.service = service;
    }

    // =========================================================
    // GET
    // =========================================================

    @GetMapping
    public List<PatientResponse> findAll(
        @RequestParam(
            required = false
        )
        String q
    ) {
        return service.search(q);
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{patientId}")
    public PatientResponse findById(
        @PathVariable
        String patientId
    ) {
        return service.findById(
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
    public PatientResponse create(
        @Valid
        @RequestBody
        CreatePatientRequest request
    ) {
        return service.create(
            request
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{patientId}")
    public PatientResponse update(
        @PathVariable
        String patientId,

        @Valid
        @RequestBody
        UpdatePatientRequest request
    ) {
        return service.update(
            patientId,
            request
        );
    }

    // =========================================================
    // DELETE / DEACTIVATE
    // =========================================================

    @DeleteMapping("/{patientId}")
    @ResponseStatus(
        HttpStatus.NO_CONTENT
    )
    public void deactivate(
        @PathVariable
        String patientId
    ) {
        service.deactivate(
            patientId
        );
    }
}