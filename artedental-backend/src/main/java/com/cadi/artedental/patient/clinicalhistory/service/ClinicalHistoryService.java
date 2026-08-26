package com.cadi.artedental.patient.clinicalhistory.service;

import com.cadi.artedental.exception.BusinessRuleException;
import com.cadi.artedental.exception.ResourceNotFoundException;
import com.cadi.artedental.patient.clinicalhistory.dto.request.SaveClinicalHistoryRequest;
import com.cadi.artedental.patient.clinicalhistory.dto.response.ClinicalHistoryResponse;
import com.cadi.artedental.patient.clinicalhistory.model.ClinicalHistory;
import com.cadi.artedental.patient.clinicalhistory.model.MedicationData;
import com.cadi.artedental.patient.clinicalhistory.repository.ClinicalHistoryRepository;
import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.patient.service.PatientService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ClinicalHistoryService {

    private final ClinicalHistoryRepository repository;
    private final PatientService patientService;
    private final Clock clock;

    public ClinicalHistoryService(
            ClinicalHistoryRepository repository,
            PatientService patientService,
            Clock clock) {
        this.repository = repository;
        this.patientService = patientService;
        this.clock = clock;
    }

    // =========================================================
    // GET
    // =========================================================

    @Transactional(readOnly = true)
    public ClinicalHistoryResponse findByPatientId(
            String patientId) {
        ClinicalHistory history = repository
                .findByPatientId(patientId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "El paciente no tiene historia clínica registrada."));

        return ClinicalHistoryResponse
                .fromEntity(history);
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Transactional
    public ClinicalHistoryResponse create(
            String patientId,
            SaveClinicalHistoryRequest request) {

        if (repository.existsByPatientId(
                patientId)) {
            throw new BusinessRuleException(
                    "El paciente ya tiene una historia clínica.");
        }

        Patient patient = patientService.getEntity(
                patientId);

        String birth = patient.getBirthDate().toString();

        OffsetDateTime now = OffsetDateTime.now(clock);

        ClinicalHistory history = new ClinicalHistory(
                UUID.randomUUID()
                        .toString(),

                patient,
                birth,
                nullable(request.gender()),
                nullable(request.bloodType()),
                nullable(request.maritalStatus()),
                nullable(request.occupation()),
                nullable(request.emergencyContactName()),
                nullable(request.emergencyContactPhone()),
                emptyToNull(request.consultationReasons()),
                nullable(request.otherConsultationReason()),
                emptyToNull(request.familyHistory()),
                nullable(request.familyHistoryNotes()),
                emptyToNull(request.pathologicalHistory()),
                nullable(request.medicationAllergies()),
                nullable(request.foodLatexAllergies()),
                toMedicationData(request.medications()),
                nullable(request.vaccinationStatus()),
                nullable(request.lastVaccine()),
                request.bisphosphonates(),
                request.previousDentalAnesthesia(),
                nullable(request.anesthesiaReaction()),
                nullable(request.lastDentalTreatmentDate()),
                nullable(request.lastDentalTreatment()),
                nullable(request.smoking()),
                nullable(request.alcohol()),
                nullable(request.drugs()),
                nullable(request.exercise()),
                nullable(request.sleepHours()),
                nullable(request.diet()),
                nullable(request.pregnancy()),
                nullable(request.breastfeeding()),
                emptyToNull(request.systems()),
                nullable(request.systemsNotes()),
                emptyToNull(request.hygieneHabits()),
                emptyToNull(request.parafunctions()),
                emptyToNull(request.extraoralFindings()),
                emptyToNull(request.intraoralFindings()),
                nullable(request.explorationNotes()),
                nullable(request.complementaryStudies()),
                nullable(request.mainDiagnosis()),
                nullable(request.secondaryDiagnosis()),
                nullable(request.prognosis()),
                nullable(request.phase1()),
                nullable(request.phase2()),
                nullable(request.phase3()),

                Boolean.TRUE.equals(
                        request.consentAccepted()),

                now,
                now);

        return ClinicalHistoryResponse
                .fromEntity(
                        repository.save(history));
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Transactional
    public ClinicalHistoryResponse update(
            String patientId,
            SaveClinicalHistoryRequest request) {

        ClinicalHistory history = repository
                .findByPatientId(patientId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "El paciente no tiene historia clínica registrada."));

        history.update(
                nullable(request.gender()),
                nullable(request.bloodType()),
                nullable(request.maritalStatus()),
                nullable(request.occupation()),
                nullable(request.emergencyContactName()),
                nullable(request.emergencyContactPhone()),
                emptyToNull(request.consultationReasons()),
                nullable(request.otherConsultationReason()),
                emptyToNull(request.familyHistory()),
                nullable(request.familyHistoryNotes()),
                emptyToNull(request.pathologicalHistory()),
                nullable(request.medicationAllergies()),
                nullable(request.foodLatexAllergies()),
                toMedicationData(request.medications()),
                nullable(request.vaccinationStatus()),
                nullable(request.lastVaccine()),

                request.bisphosphonates(),
                request.previousDentalAnesthesia(),
                nullable(request.anesthesiaReaction()),

                nullable(request.lastDentalTreatmentDate()),
                nullable(request.lastDentalTreatment()),

                nullable(request.smoking()),
                nullable(request.alcohol()),
                nullable(request.drugs()),
                nullable(request.exercise()),
                nullable(request.sleepHours()),
                nullable(request.diet()),
                nullable(request.pregnancy()),
                nullable(request.breastfeeding()),

                emptyToNull(request.systems()),
                nullable(request.systemsNotes()),
                emptyToNull(request.hygieneHabits()),
                emptyToNull(request.parafunctions()),
                emptyToNull(request.extraoralFindings()),
                emptyToNull(request.intraoralFindings()),
                nullable(request.explorationNotes()),
                nullable(request.complementaryStudies()),
                nullable(request.mainDiagnosis()),
                nullable(request.secondaryDiagnosis()),
                nullable(request.prognosis()),
                nullable(request.phase1()),
                nullable(request.phase2()),
                nullable(request.phase3()),

                Boolean.TRUE.equals(
                        request.consentAccepted()),

                OffsetDateTime.now(clock));

        return ClinicalHistoryResponse
                .fromEntity(
                        repository.save(history));
    }

    private String nullable(
            String value) {
        if (value == null ||
                value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private <K, V> Map<K, V> emptyToNull(
            Map<K, V> value) {
        return value == null || value.isEmpty() ? null : value;
    }

    private <T> Set<T> emptyToNull(
            Set<T> value) {
        return value == null || value.isEmpty() ? null : value;
    }

    private List<MedicationData> toMedicationData(
            List<MedicationData> medications) {
        if (medications == null || medications.isEmpty()) {
            return null;
        }

        return medications.stream()
                .map(medication -> new MedicationData(
                        nullable(medication.name()),
                        nullable(medication.dose()),
                        nullable(medication.frequency())))
                .toList();
    }

}