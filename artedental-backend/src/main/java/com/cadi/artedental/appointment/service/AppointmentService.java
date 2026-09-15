package com.cadi.artedental.appointment.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadi.artedental.appointment.dto.AppointmentResponse;
import com.cadi.artedental.appointment.dto.CreateAppointmentRequest;
import com.cadi.artedental.appointment.dto.UpdateAppointmentRequest;
import com.cadi.artedental.appointment.dto.UpdateAppointmentStatusRequest;
import com.cadi.artedental.appointment.model.Appointment;
import com.cadi.artedental.appointment.model.AppointmentStatus;
import com.cadi.artedental.appointment.repository.AppointmentRepository;
import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.patient.repository.PatientRepository;

@Service
public class AppointmentService {

        private final AppointmentRepository appointmentRepository;

        private final PatientRepository patientRepository;

        public AppointmentService(
                        AppointmentRepository appointmentRepository,
                        PatientRepository patientRepository) {
                this.appointmentRepository = appointmentRepository;

                this.patientRepository = patientRepository;
        }

        // =========================================================
        // CREATE
        // =========================================================

        @Transactional
        public AppointmentResponse create(
                        String patientId,
                        CreateAppointmentRequest request) {

                Patient patient = patientRepository
                                .findById(patientId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el paciente "
                                                                                + patientId));

                validateStartDateTime(
                                request.startDateTime());

                validateDuration(
                                request.durationMinutes());

                BigDecimal cost = normalizeCost(
                                request.cost());

                Appointment appointment = new Appointment();

                appointment.setPatient(
                                patient);

                appointment.setStartDateTime(
                                request.startDateTime());

                appointment.setDurationMinutes(
                                request.durationMinutes());

                appointment.setReason(
                                normalizeNullable(
                                                request.reason()));

                appointment.setStatus(
                                request.status() != null
                                                ? request.status()
                                                : AppointmentStatus.SCHEDULED);

                appointment.setCost(
                                cost);

                appointment.setNotes(
                                normalizeNullable(
                                                request.notes()));

                Appointment saved = appointmentRepository.save(
                                appointment);

                return toResponse(
                                saved);
        }

        // =========================================================
        // UPDATE
        // =========================================================

        @Transactional
        public AppointmentResponse update(
                        String patientId,
                        String appointmentId,
                        UpdateAppointmentRequest request) {

                Appointment appointment = findAppointmentForPatient(
                                patientId,
                                appointmentId);

                validateStartDateTime(
                                request.startDateTime());

                validateDuration(
                                request.durationMinutes());

                BigDecimal cost = normalizeCost(
                                request.cost());

                appointment.setStartDateTime(
                                request.startDateTime());

                appointment.setDurationMinutes(
                                request.durationMinutes());

                appointment.setReason(
                                normalizeNullable(
                                                request.reason()));

                appointment.setStatus(
                                request.status());

                appointment.setCost(
                                cost);

                appointment.setNotes(
                                normalizeNullable(
                                                request.notes()));

                Appointment saved = appointmentRepository.save(
                                appointment);

                return toResponse(
                                saved);
        }

        // =========================================================
        // UPDATE STATUS
        // =========================================================

        @Transactional
        public AppointmentResponse updateStatus(
                        String patientId,
                        String appointmentId,
                        UpdateAppointmentStatusRequest request) {

                Appointment appointment = findAppointmentForPatient(
                                patientId,
                                appointmentId);

                if (request.status() == null) {

                        throw new IllegalArgumentException(
                                        "El estatus de la cita es obligatorio.");
                }

                validateStatusTransition(
                                appointment.getStatus(),
                                request.status());

                appointment.setStatus(
                                request.status());

                Appointment saved = appointmentRepository.save(
                                appointment);

                return toResponse(
                                saved);
        }

        // =========================================================
        // FIND BY PATIENT
        // =========================================================

        @Transactional(readOnly = true)
        public List<AppointmentResponse> findByPatientId(
                        String patientId) {

                if (!patientRepository.existsById(
                                patientId)) {

                        throw new IllegalArgumentException(
                                        "No se encontró el paciente "
                                                        + patientId);
                }

                return appointmentRepository
                                .findByPatient_IdOrderByStartDateTimeDesc(
                                                patientId)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // =========================================================
        // FIND ALL
        // =========================================================

        @Transactional(readOnly = true)
        public List<AppointmentResponse> findAll() {

                return appointmentRepository
                                .findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();

        }

        // =========================================================
        // FIND BY RANGE
        // =========================================================

        @Transactional(readOnly = true)
        public List<AppointmentResponse> findByDateRange(
                        OffsetDateTime start,
                        OffsetDateTime end) {

                validateRange(
                                start,
                                end);

                return appointmentRepository
                                .findByStartDateTimeBetweenOrderByStartDateTimeAsc(
                                                start,
                                                end)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // =========================================================
        // FIND PATIENT APPOINTMENTS BY RANGE
        // =========================================================

        @Transactional(readOnly = true)
        public List<AppointmentResponse> findByPatientAndDateRange(
                        String patientId,
                        OffsetDateTime start,
                        OffsetDateTime end) {

                if (!patientRepository.existsById(
                                patientId)) {

                        throw new IllegalArgumentException(
                                        "No se encontró el paciente "
                                                        + patientId);
                }

                validateRange(
                                start,
                                end);

                return appointmentRepository
                                .findByPatient_IdAndStartDateTimeBetweenOrderByStartDateTimeAsc(
                                                patientId,
                                                start,
                                                end)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // =========================================================
        // FIND ONE
        // =========================================================

        @Transactional(readOnly = true)
        public AppointmentResponse findById(
                        String patientId,
                        String appointmentId) {

                Appointment appointment = findAppointmentForPatient(
                                patientId,
                                appointmentId);

                return toResponse(
                                appointment);
        }

        // =========================================================
        // INTERNAL FIND
        // =========================================================

        private Appointment findAppointmentForPatient(
                        String patientId,
                        String appointmentId) {

                Appointment appointment = appointmentRepository
                                .findById(
                                                appointmentId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró la cita."));

                if (appointment.getPatient() == null ||
                                appointment
                                                .getPatient()
                                                .getId() == null
                                ||
                                !appointment
                                                .getPatient()
                                                .getId()
                                                .equals(patientId)) {

                        throw new IllegalArgumentException(
                                        "La cita no pertenece al paciente.");
                }

                return appointment;
        }

        // =========================================================
        // STATUS TRANSITIONS
        // =========================================================

        private void validateStatusTransition(
                        AppointmentStatus current,
                        AppointmentStatus next) {

                if (current == null) {
                        return;
                }

                if (current == next) {
                        return;
                }

                switch (current) {

                        case SCHEDULED -> {

                                if (next != AppointmentStatus.IN_PROGRESS &&
                                                next != AppointmentStatus.COMPLETED &&
                                                next != AppointmentStatus.CANCELLED &&
                                                next != AppointmentStatus.NO_SHOW) {

                                        throw new IllegalStateException(
                                                        "Transición de estatus no permitida.");
                                }
                        }

                        case IN_PROGRESS -> {

                                if (next != AppointmentStatus.COMPLETED &&
                                                next != AppointmentStatus.CANCELLED &&
                                                next != AppointmentStatus.NO_SHOW) {

                                        throw new IllegalStateException(
                                                        "Transición de estatus no permitida.");
                                }
                        }

                        case COMPLETED -> {

                                throw new IllegalStateException(
                                                "Una cita completada ya no puede cambiar de estatus.");
                        }

                        case CANCELLED -> {

                                throw new IllegalStateException(
                                                "Una cita cancelada ya no puede cambiar de estatus.");
                        }

                        case NO_SHOW -> {

                                throw new IllegalStateException(
                                                "Una cita marcada como no asistió "
                                                                + "ya no puede cambiar de estatus.");
                        }
                }
        }

        // =========================================================
        // VALIDATIONS
        // =========================================================

        private void validateStartDateTime(
                        OffsetDateTime startDateTime) {

                if (startDateTime == null) {

                        throw new IllegalArgumentException(
                                        "La fecha y hora de la cita son obligatorias.");
                }
        }

        private void validateDuration(
                        Integer durationMinutes) {

                if (durationMinutes == null ||
                                durationMinutes <= 0) {

                        throw new IllegalArgumentException(
                                        "La duración debe ser mayor a 0 minutos.");
                }
        }

        private BigDecimal normalizeCost(
                        BigDecimal cost) {

                if (cost == null) {
                        return BigDecimal.ZERO;
                }

                if (cost.compareTo(
                                BigDecimal.ZERO) < 0) {

                        throw new IllegalArgumentException(
                                        "El costo de la cita no puede ser negativo.");
                }

                return cost;
        }

        private void validateRange(
                        OffsetDateTime start,
                        OffsetDateTime end) {

                if (start == null ||
                                end == null) {

                        throw new IllegalArgumentException(
                                        "La fecha inicial y final son obligatorias.");
                }

                if (end.isBefore(start)) {

                        throw new IllegalArgumentException(
                                        "La fecha final no puede ser anterior "
                                                        + "a la fecha inicial.");
                }
        }

        // =========================================================
        // NORMALIZATION
        // =========================================================

        private String normalizeNullable(
                        String value) {

                if (value == null) {
                        return null;
                }

                String trimmed = value.trim();

                return trimmed.isEmpty()
                                ? null
                                : trimmed;
        }

        // =========================================================
        // RESPONSE
        // =========================================================

        private AppointmentResponse toResponse(
                        Appointment appointment) {

                Patient patient = appointment.getPatient();

                String patientId = patient != null
                                ? patient.getId()
                                : null;

                String patientName = buildPatientName(
                                patient);

                return new AppointmentResponse(
                                appointment.getId(),
                                patientId,
                                patientName,
                                appointment.getStartDateTime(),
                                appointment.getDurationMinutes(),
                                appointment.getReason(),
                                appointment.getStatus(),
                                appointment.getCost(),
                                appointment.getNotes(),
                                appointment.getCreatedAt(),
                                appointment.getUpdatedAt());
        }

        // =========================================================
        // PATIENT NAME
        // =========================================================

        private String buildPatientName(
                        Patient patient) {

                if (patient == null) {
                        return "";
                }

                StringBuilder name = new StringBuilder();

                if (patient.getNames() != null &&
                                !patient.getNames().isBlank()) {
                        name.append(
                                        patient.getNames().trim());
                }

                if (patient.getLastName() != null &&
                                !patient.getLastName().isBlank()) {

                        if (name.length() > 0) {
                                name.append(" ");
                        }

                        name.append(
                                        patient.getLastName().trim());
                }

                if (patient.getFirstName() != null &&
                                !patient.getFirstName().isBlank()) {

                        if (name.length() > 0) {
                                name.append(" ");
                        }

                        name.append(
                                        patient.getFirstName().trim());
                }

                return name.toString();
        }
}