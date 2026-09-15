package com.cadi.artedental.appointment.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cadi.artedental.appointment.dto.AppointmentResponse;
import com.cadi.artedental.appointment.dto.CreateAppointmentRequest;
import com.cadi.artedental.appointment.dto.UpdateAppointmentRequest;
import com.cadi.artedental.appointment.dto.UpdateAppointmentStatusRequest;
import com.cadi.artedental.appointment.service.AppointmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
@Validated
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<AppointmentResponse> create(
            @PathVariable String patientId,

            @Valid @RequestBody CreateAppointmentRequest request) {

        AppointmentResponse response = appointmentService.create(
                patientId,
                request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // =========================================================
    // GET ALL BY PATIENT
    // =========================================================

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> findByPatient(
            @PathVariable String patientId) {

        return ResponseEntity.ok(
                appointmentService
                        .findByPatientId(
                                patientId));
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping("/all")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService
                        .findAll());
    }

    // =========================================================
    // GET ONE
    // =========================================================

    @GetMapping("/patient/{patientId}/{appointmentId}")
    public ResponseEntity<AppointmentResponse> findById(
            @PathVariable String patientId,
            @PathVariable String appointmentId) {

        return ResponseEntity.ok(
                appointmentService.findById(
                        patientId,
                        appointmentId));
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/patient/{patientId}/appointment/{appointmentId}")
    public ResponseEntity<AppointmentResponse> update(
            @PathVariable String patientId,
            @PathVariable String appointmentId,

            @Valid @RequestBody UpdateAppointmentRequest request) {

        return ResponseEntity.ok(
                appointmentService.update(
                        patientId,
                        appointmentId,
                        request));
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable String patientId,
            @PathVariable String appointmentId,

            @Valid @RequestBody UpdateAppointmentStatusRequest request) {

        return ResponseEntity.ok(
                appointmentService.updateStatus(
                        patientId,
                        appointmentId,
                        request));
    }

    // =========================================================
    // GET PATIENT APPOINTMENTS BY DATE RANGE
    // =========================================================

    @GetMapping("/range")
    public ResponseEntity<List<AppointmentResponse>> findByPatientAndDateRange(
            @PathVariable String patientId,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end) {

        return ResponseEntity.ok(
                appointmentService
                        .findByPatientAndDateRange(
                                patientId,
                                start,
                                end));
    }
}