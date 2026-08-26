package com.cadi.artedental.patient.odontogram.service;

import com.cadi.artedental.exception.BusinessRuleException;
import com.cadi.artedental.exception.ResourceNotFoundException;

import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.patient.service.PatientService;

import com.cadi.artedental.patient.odontogram.dto.request.SaveToothRequest;
import com.cadi.artedental.patient.odontogram.dto.response.ToothResponse;
import com.cadi.artedental.patient.odontogram.model.PatientTooth;
import com.cadi.artedental.patient.odontogram.repository.PatientToothRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OdontogramService {

    private final PatientToothRepository repository;
    private final PatientService patientService;
    private final Clock clock;

    public OdontogramService(
            PatientToothRepository repository,
            PatientService patientService,
            Clock clock) {
        this.repository = repository;
        this.patientService = patientService;
        this.clock = clock;
    }

    // =========================================================
    // GET ODONTOGRAM
    // =========================================================

    @Transactional(readOnly = true)
    public List<ToothResponse> findByPatientId(
            String patientId) {

        // Verificamos que el paciente exista.
        patientService.getEntity(patientId);

        return repository
                .findByPatientIdOrderByToothNumberAsc(
                        patientId)
                .stream()
                .map(
                        ToothResponse::fromEntity)
                .toList();
    }

    // =========================================================
    // CREATE OR UPDATE TOOTH
    // =========================================================

    @Transactional
    public ToothResponse saveTooth(
            String patientId,
            String toothNumber,
            SaveToothRequest request) {

        validateToothNumber(
                toothNumber);

        Patient patient = patientService.getEntity(
                patientId);

        OffsetDateTime now = OffsetDateTime.now(clock);

        PatientTooth tooth = repository
                .findByPatientIdAndToothNumber(
                        patientId,
                        toothNumber)
                .orElse(null);

        if (tooth == null) {

            tooth = new PatientTooth(UUID.randomUUID().toString(),
                    patient,
                    toothNumber,
                    request.dentitionType(),
                    request.toothType(),
                    request.status(),
                    request.diagnosis(),
                    request.notes(),
                    now,
                    now);

        } else {

            tooth.update(
                    request.dentitionType(),
                    request.toothType(),
                    request.status(),
                    request.diagnosis(),
                    request.notes(),
                    now);
        }

        PatientTooth saved = repository.save(tooth);

        return ToothResponse.fromEntity(
                saved);
    }

    // =========================================================
    // DELETE / RESET TOOTH
    // =========================================================

    @Transactional
    public void deleteTooth(
            String patientId,
            String toothNumber) {

        PatientTooth tooth = repository
                .findByPatientIdAndToothNumber(
                        patientId,
                        toothNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "No existe información registrada "
                                        + "para el diente "
                                        + toothNumber
                                        + "."));

        repository.delete(tooth);
    }

    // =========================================================
    // VALIDATION
    // =========================================================
    private void validateToothNumber(
            String toothNumber) {
        if (toothNumber == null ||
                toothNumber.isBlank()) {
            throw new BusinessRuleException(
                    "El número de diente es obligatorio.");
        }

        int value;

        try {
            value = Integer.parseInt(
                    toothNumber);
        } catch (NumberFormatException e) {
            throw new BusinessRuleException(
                    "Número de diente inválido: "
                            + toothNumber);
        }

        int quadrant = value / 10;
        int position = value % 10;

        // Dentición permanente:
        // 11-18, 21-28, 31-38, 41-48
        boolean permanent = quadrant >= 1 &&
                quadrant <= 4 &&
                position >= 1 &&
                position <= 8;

        // Dentición temporal:
        // 51-55, 61-65, 71-75, 81-85
        boolean primary = quadrant >= 5 &&
                quadrant <= 8 &&
                position >= 1 &&
                position <= 5;

        if (!permanent && !primary) {
            throw new BusinessRuleException(
                    "Número de diente inválido: "
                            + toothNumber);
        }
    }

    private String normalizeNotes(
            String notes) {
        if (notes == null ||
                notes.isBlank()) {
            return null;
        }

        return notes.trim();
    }
}