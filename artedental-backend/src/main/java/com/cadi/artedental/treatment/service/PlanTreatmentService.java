package com.cadi.artedental.treatment.service;

import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.patient.odontogram.model.PatientTooth;
import com.cadi.artedental.patient.odontogram.model.ToothStatus;
import com.cadi.artedental.patient.odontogram.repository.PatientToothRepository;
import com.cadi.artedental.patient.repository.PatientRepository;
import com.cadi.artedental.treatment.dto.request.CreatePlanTreatmentRequest;
import com.cadi.artedental.treatment.dto.request.CreateTreatmentRequest;
import com.cadi.artedental.treatment.dto.request.UpdateTreatmentRequest;
import com.cadi.artedental.treatment.dto.response.PlanTreatmentResponse;
import com.cadi.artedental.treatment.dto.response.TreatmentResponse;
import com.cadi.artedental.treatment.model.Treatment;
import com.cadi.artedental.treatment.model.TreatmentPlan;
import com.cadi.artedental.treatment.model.TreatmentStatus;
import com.cadi.artedental.treatment.repository.PlanTreatmentRepository;
import com.cadi.artedental.treatment.repository.TreatmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PlanTreatmentService {

        private final PlanTreatmentRepository treatmentRepository;
        private final PatientRepository patientRepository;
        private final PatientToothRepository patientToothRepository;

        public PlanTreatmentService(
                        PlanTreatmentRepository treatmentRepository,
                        PatientRepository patientRepository,
                        PatientToothRepository patientToothRepository) {
                this.treatmentRepository = treatmentRepository;
                this.patientRepository = patientRepository;
                this.patientToothRepository = patientToothRepository;
        }

        // =========================================================
        // GET POR PACIENTE
        // =========================================================

        @Transactional(readOnly = true)
        public List<PlanTreatmentResponse> findByPatientId(
                        String patientId) {
                requirePatient(patientId);

                return treatmentRepository
                                .findByPatient_IdOrderByCreatedAtDesc(patientId)
                                .stream()
                                .map(PlanTreatmentResponse::fromEntity)
                                .toList();
        }

        // =========================================================
        // GET POR ID
        // =========================================================

        @Transactional(readOnly = true)
        public PlanTreatmentResponse findById(
                        String patientId,
                        String treatmentId) {
                TreatmentPlan treatment = requireTreatment(
                                patientId,
                                treatmentId);

                return PlanTreatmentResponse.fromEntity(
                                treatment);
        }

        // =========================================================
        // GET POR DIENTE
        // =========================================================

        @Transactional(readOnly = true)
        public List<PlanTreatmentResponse> findByToothNumber(
                        String patientId,
                        String toothNumber) {
                requirePatient(patientId);

                String normalizedToothNumber = normalizeToothNumber(toothNumber);

                return treatmentRepository
                                .findByPatientIdAndToothNumber(
                                                patientId,
                                                normalizedToothNumber)
                                .stream()
                                .map(PlanTreatmentResponse::fromEntity)
                                .toList();
        }

        // =========================================================
        // CREATE
        // =========================================================

        public PlanTreatmentResponse create(
                        String patientId,
                        CreatePlanTreatmentRequest request) {
                Patient patient = requirePatient(patientId);

                PatientTooth patientTooth = resolvePatientTooth(
                                patient,
                                request.toothNumber());

                BigDecimal cost = defaultMoney(request.cost());

                BigDecimal amountPaid = defaultMoney(request.amountPaid());

                BigDecimal progress = defaultProgress(request.progress());

                TreatmentStatus status = request.status() == null
                                ? TreatmentStatus.IN_PROGRESS
                                : request.status();

                OffsetDateTime startDate = request.startDate() == null
                                ? OffsetDateTime.now()
                                : request.startDate();

                validateFinancialData(
                                cost,
                                amountPaid);

                validateProgress(progress);

                OffsetDateTime completedDate = status == TreatmentStatus.COMPLETED
                                ? OffsetDateTime.now()
                                : null;

                if (status == TreatmentStatus.COMPLETED) {
                        progress = BigDecimal.ONE;
                }

                TreatmentPlan treatment = new TreatmentPlan(
                                UUID.randomUUID().toString(),

                                patient,

                                patientTooth,

                                normalizeRequiredText(
                                                request.treatmentName()),

                                nullable(
                                                request.description()),

                                cost,

                                amountPaid,

                                progress,

                                status,

                                nullable(
                                                request.notes())


                                );

                TreatmentPlan saved = treatmentRepository.save(treatment);

                return PlanTreatmentResponse.fromEntity(
                                saved);
        }

        // =========================================================
        // UPDATE
        // =========================================================

        public PlanTreatmentResponse update(
                        Patient patient,
                        String patientId,
                        String treatmentId,
                        UpdateTreatmentRequest request) {

                TreatmentPlan existingTreatment = treatmentRepository
                                .findById(treatmentId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el tratamiento "
                                                                                + treatmentId));

                patient = existingTreatment.getPatient();

                if (patient == null) {
                        throw new IllegalStateException(
                                        "El tratamiento "
                                                        + treatmentId
                                                        + " no tiene paciente asociado.");
                }

                if (!patient.getId().equals(patientId)) {
                        throw new IllegalArgumentException(
                                        "El tratamiento no pertenece al paciente "
                                                        + patientId);
                }

                TreatmentPlan treatment = requireTreatment(
                                patient.getId(),
                                treatmentId);

                PatientTooth patientTooth = resolvePatientTooth(
                                patient,
                                request.toothNumber());

                BigDecimal cost = defaultMoney(request.cost());

                BigDecimal amountPaid = defaultMoney(request.amountPaid());

                BigDecimal progress = defaultProgress(request.progress());

                TreatmentStatus status = request.status() == null
                                ? treatment.getStatus()
                                : request.status();

                validateFinancialData(
                                cost,
                                amountPaid);

                validateProgress(progress);


                if (status == TreatmentStatus.COMPLETED) {
                        progress = BigDecimal.ONE;
                }

                treatment.update(
                                patientTooth,

                                normalizeRequiredText(
                                                request.treatmentName()),

                                nullable(
                                                request.description()),

                                cost,

                                amountPaid,

                                progress,

                                status,

                                nullable(
                                                request.notes()),

                                request.startDate()

                                );

                TreatmentPlan saved = treatmentRepository.save(treatment);

                return PlanTreatmentResponse.fromEntity(
                                saved);
        }

        // =========================================================
        // DELETE
        // =========================================================

        public void delete(
                        String patientId,
                        String treatmentId) {
                TreatmentPlan treatment = requireTreatment(
                                patientId,
                                treatmentId);

                treatmentRepository.delete(treatment);
        }

        // =========================================================
        // REQUIRE PATIENT
        // =========================================================

        private Patient requirePatient(
                        String patientId) {
                if (patientId == null ||
                                patientId.isBlank()) {
                        throw new IllegalArgumentException(
                                        "patientId es obligatorio.");
                }

                return patientRepository
                                .findById(patientId.trim())
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el paciente: "
                                                                                + patientId));
        }

        // =========================================================
        // REQUIRE TREATMENT
        // =========================================================

        private TreatmentPlan requireTreatment(
                        String patientId,
                        String treatmentId) {
                if (treatmentId == null ||
                                treatmentId.isBlank()) {
                        throw new IllegalArgumentException(
                                        "treatmentId es obligatorio.");
                }

                return treatmentRepository
                                .findByIdAndPatient_Id(
                                                treatmentId.trim(),
                                                patientId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el tratamiento "
                                                                                + treatmentId
                                                                                + " para el paciente "
                                                                                + patientId));
        }

        // =========================================================
        // RESOLVER DIENTE
        // =========================================================
        private void validateToothNumber(
                        String toothNumber) {

                if (toothNumber == null ||
                                !toothNumber.matches("\\d{2}")) {

                        throw new IllegalArgumentException(
                                        "Número de diente inválido: "
                                                        + toothNumber);
                }

                int quadrant = Character.getNumericValue(
                                toothNumber.charAt(0));

                int position = Character.getNumericValue(
                                toothNumber.charAt(1));

                boolean permanent = quadrant >= 1 &&
                                quadrant <= 4 &&
                                position >= 1 &&
                                position <= 8;

                boolean primary = quadrant >= 5 &&
                                quadrant <= 8 &&
                                position >= 1 &&
                                position <= 5;

                if (!permanent && !primary) {
                        throw new IllegalArgumentException(
                                        "Número de diente FDI inválido: "
                                                        + toothNumber);
                }
        }

        private String resolveDentitionType(
                        String toothNumber) {

                validateToothNumber(toothNumber);

                int quadrant = Character.getNumericValue(
                                toothNumber.charAt(0));

                if (quadrant >= 1 && quadrant <= 4) {
                        return "PERMANENT";
                }

                if (quadrant >= 5 && quadrant <= 8) {
                        return "PRIMARY";
                }

                throw new IllegalArgumentException(
                                "Número de diente inválido: "
                                                + toothNumber);
        }

        private PatientTooth createPatientTooth(
                        Patient patient,
                        String toothNumber) {

                OffsetDateTime now = OffsetDateTime.now();

                PatientTooth patientTooth = new PatientTooth(
                                UUID.randomUUID().toString(),
                                patient,
                                toothNumber,
                                resolveDentitionType(toothNumber),
                                resolveToothType(toothNumber),
                                ToothStatus.HEALTHY,
                                null,
                                null,
                                now,
                                now);

                return patientToothRepository.save(
                                patientTooth);
        }

        private PatientTooth resolvePatientTooth(
                        Patient patient,
                        String toothNumber) {

                if (toothNumber == null ||
                                toothNumber.isBlank()) {
                        return null;
                }

                String normalizedToothNumber = toothNumber.trim();

                return patientToothRepository
                                .findByPatient_IdAndToothNumber(
                                                patient.getId(),
                                                normalizedToothNumber)
                                .orElseGet(() -> createPatientTooth(
                                                patient,
                                                normalizedToothNumber));
        }

        // =========================================================
        // VALIDAR NÚMERO DENTAL
        // =========================================================

        private String normalizeToothNumber(
                        String toothNumber) {
                if (toothNumber == null ||
                                toothNumber.isBlank()) {
                        throw new IllegalArgumentException(
                                        "Número de diente inválido.");
                }

                String value = toothNumber.trim();

                if (!value.matches("\\d{2}")) {
                        throw new IllegalArgumentException(
                                        "Número de diente inválido: "
                                                        + value);
                }

                return value;
        }

        // =========================================================
        // FINANZAS
        // =========================================================

        private void validateFinancialData(
                        BigDecimal cost,
                        BigDecimal amountPaid) {
                if (cost.compareTo(BigDecimal.ZERO) < 0) {
                        throw new IllegalArgumentException(
                                        "El costo no puede ser negativo.");
                }

                if (amountPaid.compareTo(BigDecimal.ZERO) < 0) {
                        throw new IllegalArgumentException(
                                        "El monto pagado no puede ser negativo.");
                }

                if (amountPaid.compareTo(cost) > 0) {
                        throw new IllegalArgumentException(
                                        "El monto pagado no puede ser mayor "
                                                        + "al costo del tratamiento.");
                }
        }

        // =========================================================
        // PROGRESS
        // =========================================================

        private void validateProgress(
                        BigDecimal progress) {
                if (progress.compareTo(BigDecimal.ZERO) < 0 ||
                                progress.compareTo(BigDecimal.ONE) > 0) {

                        throw new IllegalArgumentException(
                                        "El avance debe estar entre 0 y 1.");
                }
        }



        // =========================================================
        // HELPERS
        // =========================================================

        private BigDecimal defaultMoney(
                        BigDecimal value) {
                return value == null
                                ? BigDecimal.ZERO
                                : value;
        }

        private BigDecimal defaultProgress(
                        BigDecimal value) {
                return value == null
                                ? BigDecimal.ZERO
                                : value;
        }

        private String normalizeRequiredText(
                        String value) {
                if (value == null ||
                                value.isBlank()) {
                        throw new IllegalArgumentException(
                                        "El nombre del tratamiento es obligatorio.");
                }

                return value.trim();
        }

        private String nullable(
                        String value) {
                if (value == null) {
                        return null;
                }

                String normalized = value.trim();

                return normalized.isEmpty()
                                ? null
                                : normalized;
        }

        private String resolveToothType(
                        String toothNumber) {
                validateToothNumber(toothNumber);

                int quadrant = Character.getNumericValue(
                                toothNumber.charAt(0));

                int position = Character.getNumericValue(
                                toothNumber.charAt(1));

                // DENTICIÓN TEMPORAL
                if (quadrant >= 5 && quadrant <= 8) {
                        return switch (position) {
                                case 1, 2 -> "INCISOR";
                                case 3 -> "CANINE";
                                case 4, 5 -> "MOLAR";

                                default -> throw new IllegalArgumentException(
                                                "Número de diente temporal inválido: "
                                                                + toothNumber);
                        };
                }

                // DENTICIÓN PERMANENTE
                return switch (position) {
                        case 1, 2 -> "INCISOR";
                        case 3 -> "CANINE";
                        case 4, 5 -> "PREMOLAR";
                        case 6, 7, 8 -> "MOLAR";

                        default -> throw new IllegalArgumentException(
                                        "Número de diente permanente inválido: "
                                                        + toothNumber);
                };
        }

}