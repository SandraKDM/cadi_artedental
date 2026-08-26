package com.cadi.artedental.patient.odontogram.controller;

import com.cadi.artedental.patient.odontogram.dto.request.SaveToothRequest;
import com.cadi.artedental.patient.odontogram.dto.response.ToothResponse;
import com.cadi.artedental.patient.odontogram.service.OdontogramService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/odontogram")
public class OdontogramController {

    private final OdontogramService service;

    public OdontogramController(
            OdontogramService service) {
        this.service = service;
    }

    // =========================================================
    // GET ODONTOGRAM
    // =========================================================

    @GetMapping
    public List<ToothResponse> getOdontogram(
            @PathVariable String patientId) {
        return service.findByPatientId(
                patientId);
    }

    // =========================================================
    // CREATE / UPDATE TOOTH
    // =========================================================

    @PutMapping("/{toothNumber}")
    public ToothResponse saveTooth(
            @PathVariable String patientId,
            @PathVariable String toothNumber,
            @Valid @RequestBody SaveToothRequest request) {
        return service.saveTooth(
                patientId,
                toothNumber,
                request);
    }

    // =========================================================
    // DELETE / RESET TOOTH
    // =========================================================

    @DeleteMapping("/{toothNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTooth(
            @PathVariable String patientId,

            @PathVariable String toothNumber) {
        service.deleteTooth(
                patientId,
                toothNumber);
    }
}