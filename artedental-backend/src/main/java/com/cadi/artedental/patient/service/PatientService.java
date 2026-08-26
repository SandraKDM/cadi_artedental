package com.cadi.artedental.patient.service;

import com.cadi.artedental.exception.BusinessRuleException;
import com.cadi.artedental.exception.ResourceNotFoundException;
import com.cadi.artedental.patient.dto.request.CreatePatientRequest;
import com.cadi.artedental.patient.dto.request.UpdatePatientRequest;
import com.cadi.artedental.patient.dto.response.PatientResponse;
import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.patient.repository.PatientRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository repository;
    private final Clock clock;

    public PatientService(
        PatientRepository repository,
        Clock clock
    ) {
        this.repository = repository;
        this.clock = clock;
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Transactional(readOnly = true)
    public List<PatientResponse> findAll() {
        return repository
            .findByActiveTrueOrderByFirstNameAsc()
            .stream()
            .map(
                PatientResponse::fromEntity
            )
            .toList();
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public PatientResponse findById(
        String patientId
    ) {
        return PatientResponse.fromEntity(
            getEntity(patientId)
        );
    }

    // =========================================================
    // SEARCH
    // =========================================================

    @Transactional(readOnly = true)
    public List<PatientResponse> search(
        String query
    ) {
        if (
            query == null ||
            query.isBlank()
        ) {
            return findAll();
        }

        String normalized =
            query.trim();

        String phone =
            normalized.replaceAll(
                "\\D",
                ""
            );

        return repository
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCase(
                normalized,
                normalized,
                phone,
                normalized
            )
            .stream()
            .filter(
                Patient::isActive
            )
            .map(
                PatientResponse::fromEntity
            )
            .toList();
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Transactional
    public PatientResponse create(
        CreatePatientRequest request
    ) {

        String phone =
            normalizePhone(
                request.phone()
            );

        if (
            repository.existsByPhone(phone)
        ) {
            throw new BusinessRuleException(
                "Ya existe un paciente con ese teléfono."
            );
        }

        OffsetDateTime now =
            OffsetDateTime.now(clock);

        Patient patient =
            new Patient(
                UUID.randomUUID()
                    .toString(),

                request
                    .names()
                    .trim(),

                request
                    .firstName()
                    .trim(),

                request
                    .lastName()
                    .trim(),

                phone,

                normalizeEmail(
                    request.email()
                ),

                request.birthDate(),

                normalizeNullable(
                    request.allergies()
                ),

                normalizeNullable(
                    request.address()
                ),

                normalizeNullable(
                    request.notes()
                ),

                true,

                now,
                now
            );

        Patient saved =
            repository.save(patient);

        return PatientResponse
            .fromEntity(saved);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Transactional
    public PatientResponse update(
        String patientId,
        UpdatePatientRequest request
    ) {

        Patient patient =
            getEntity(patientId);

        patient.update(
            request.names().trim(),
            request.firstName().trim(),
            request.lastName().trim(),
            normalizePhone(
                request.phone()
            ),
            normalizeEmail(
                request.email()
            ),
            request.birthDate(),
            normalizeNullable(
                request.allergies()
            ),
            normalizeNullable(
                request.address()
            ),
            normalizeNullable(
                request.notes()
            ),
            OffsetDateTime.now(clock)
        );

        return PatientResponse.fromEntity(
            repository.save(patient)
        );
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Transactional
    public void deactivate(
        String patientId
    ) {
        Patient patient =
            getEntity(patientId);

        patient.deactivate(
            OffsetDateTime.now(clock)
        );

        repository.save(patient);
    }

    // =========================================================
    // INTERNAL
    // =========================================================

    @Transactional(readOnly = true)
    public Patient getEntity(
        String patientId
    ) {
        return repository
            .findById(patientId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        "No se encontró el paciente "
                            + patientId
                            + "."
                    )
            );
    }

    private String normalizePhone(
        String phone
    ) {
        String normalized =
            phone.replaceAll(
                "\\D",
                ""
            );

        if (normalized.length() < 10) {
            throw new BusinessRuleException(
                "El teléfono debe contener al menos 10 dígitos."
            );
        }

        return normalized;
    }

    private String normalizeEmail(
        String email
    ) {
        if (
            email == null ||
            email.isBlank()
        ) {
            return null;
        }

        return email
            .trim()
            .toLowerCase();
    }

private String normalizeNullable(
    String value
) {
    if (
        value == null ||
        value.isBlank()
    ) {
        return null;
    }

    return value.trim();
}
}