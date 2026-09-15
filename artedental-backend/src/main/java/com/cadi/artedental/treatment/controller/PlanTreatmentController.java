package com.cadi.artedental.treatment.controller;
import com.cadi.artedental.treatment.dto.request.CreatePlanTreatmentRequest;
import com.cadi.artedental.treatment.dto.request.UpdateTreatmentRequest;
import com.cadi.artedental.treatment.dto.response.PlanTreatmentResponse;
import com.cadi.artedental.treatment.service.PlanTreatmentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/plan")
public class PlanTreatmentController {

    private final PlanTreatmentService treatmentService;

    public PlanTreatmentController(
        PlanTreatmentService treatmentService
    ) {
        this.treatmentService = treatmentService;
    }

    // =========================================================
    // GET - TODOS LOS TRATAMIENTOS DEL PACIENTE
    // =========================================================

    @GetMapping
    public ResponseEntity<List<PlanTreatmentResponse>>
        findByPatientId(
            @PathVariable String patientId,

            @RequestParam(
                required = false
            )
            String toothNumber
        ) {

        if (toothNumber != null &&
            !toothNumber.isBlank()) {

            return ResponseEntity.ok(
                treatmentService.findByToothNumber(
                    patientId,
                    toothNumber
                )
            );
        }

        return ResponseEntity.ok(
            treatmentService.findByPatientId(
                patientId
            )
        );
    }

    // =========================================================
    // GET - TRATAMIENTO POR ID
    // =========================================================

    @GetMapping("/{treatmentId}")
    public ResponseEntity<PlanTreatmentResponse>
        findById(
            @PathVariable String patientId,
            @PathVariable String treatmentId
        ) {

        return ResponseEntity.ok(
            treatmentService.findById(
                patientId,
                treatmentId
            )
        );
    }

    // =========================================================
    // POST - CREAR TRATAMIENTO
    // =========================================================

    @PostMapping
    public ResponseEntity<PlanTreatmentResponse>
        create(
            @PathVariable String patientId,

            @Valid
            @RequestBody
            CreatePlanTreatmentRequest request
        ) {

        PlanTreatmentResponse response =
            treatmentService.create(
                patientId,
                request
            );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response);
    }

    // =========================================================
    // PUT - ACTUALIZAR TRATAMIENTO
    // =========================================================

    @PutMapping("/{treatmentId}")
    public ResponseEntity<PlanTreatmentResponse>
        update(
            @PathVariable String patientId,
            @PathVariable String treatmentId,

            @Valid
            @RequestBody
            UpdateTreatmentRequest request
        ) {

        return ResponseEntity.ok(
            treatmentService.update(
                null, patientId,
                treatmentId,
                request
            )
        );
    }

    // =========================================================
    // DELETE - ELIMINAR TRATAMIENTO
    // =========================================================

    @DeleteMapping("/{treatmentId}")
    public ResponseEntity<Void>
        delete(
            @PathVariable String patientId,
            @PathVariable String treatmentId
        ) {

        treatmentService.delete(
            patientId,
            treatmentId
        );

        return ResponseEntity.noContent().build();
    }
}