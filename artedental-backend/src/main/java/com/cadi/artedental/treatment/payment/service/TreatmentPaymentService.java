package com.cadi.artedental.treatment.payment.service;

import com.cadi.artedental.treatment.model.Treatment;
import com.cadi.artedental.treatment.payment.dto.PatientFinancialSummaryResponse;
import com.cadi.artedental.treatment.payment.dto.TreatmentDebtResponse;
import com.cadi.artedental.treatment.payment.dto.TreatmentPaymentRequest;
import com.cadi.artedental.treatment.payment.dto.TreatmentPaymentResponse;
import com.cadi.artedental.treatment.payment.model.TreatmentPayment;
import com.cadi.artedental.treatment.payment.repository.TreatmentPaymentRepository;
import com.cadi.artedental.treatment.repository.TreatmentRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TreatmentPaymentService {

        private final TreatmentPaymentRepository paymentRepository;

        private final TreatmentRepository treatmentRepository;

        public TreatmentPaymentService(
                        TreatmentPaymentRepository paymentRepository,
                        TreatmentRepository treatmentRepository) {
                this.paymentRepository = paymentRepository;
                this.treatmentRepository = treatmentRepository;
        }

        // =========================================================
        // REGISTER PAYMENT
        // =========================================================

        @Transactional
        public TreatmentPaymentResponse registerPayment(
                        String treatmentId,
                        TreatmentPaymentRequest request) {

                // -----------------------------------------------------
                // 1. Buscar tratamiento
                // -----------------------------------------------------

                Treatment treatment = treatmentRepository
                                .findById(treatmentId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el tratamiento "
                                                                                + treatmentId));

                // -----------------------------------------------------
                // 2. Validar monto
                // -----------------------------------------------------

                BigDecimal paymentAmount = request.amount();

                if (paymentAmount == null) {
                        throw new IllegalArgumentException(
                                        "El monto del pago es obligatorio.");
                }

                if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException(
                                        "El monto del pago debe ser mayor que cero.");
                }

                // -----------------------------------------------------
                // 3. Obtener costo y total pagado
                // -----------------------------------------------------

                BigDecimal cost = safe(treatment.getCost());

                BigDecimal currentPaid = safe(treatment.getAmountPaid());

                BigDecimal pending = cost.subtract(currentPaid);

                // Evitamos pendientes negativos por datos antiguos.
                if (pending.compareTo(BigDecimal.ZERO) < 0) {
                        pending = BigDecimal.ZERO;
                }

                // -----------------------------------------------------
                // 4. Validar que no pague más del adeudo
                // -----------------------------------------------------

                if (paymentAmount.compareTo(pending) > 0) {
                        throw new IllegalArgumentException(
                                        "El pago no puede superar el saldo pendiente. "
                                                        + "Saldo pendiente: "
                                                        + pending);
                }

                if (pending.compareTo(BigDecimal.ZERO) == 0) {
                        throw new IllegalArgumentException(
                                        "El tratamiento no tiene saldo pendiente.");
                }

                // -----------------------------------------------------
                // 5. Crear pago
                // -----------------------------------------------------

                TreatmentPayment payment = new TreatmentPayment();

                payment.setTreatment(treatment);

                payment.setAmount(paymentAmount);

                payment.setPaymentDate(
                                request.paymentDate() != null
                                                ? request.paymentDate()
                                                : OffsetDateTime.now());

                payment.setPaymentMethod(
                                nullable(request.paymentMethod()));

                payment.setReference(
                                nullable(request.reference()));

                payment.setNotes(
                                nullable(request.notes()));

                TreatmentPayment savedPayment = paymentRepository.save(payment);

                // -----------------------------------------------------
                // 6. Actualizar amountPaid
                // -----------------------------------------------------

                BigDecimal newTotalPaid = currentPaid.add(paymentAmount);

                treatment.setAmountPaid(
                                newTotalPaid);

                treatmentRepository.save(treatment);

                // -----------------------------------------------------
                // 7. Calcular nuevo pendiente
                // -----------------------------------------------------

                BigDecimal newPending = cost.subtract(newTotalPaid);

                if (newPending.compareTo(BigDecimal.ZERO) < 0) {
                        newPending = BigDecimal.ZERO;
                }

                // -----------------------------------------------------
                // 8. Response
                // -----------------------------------------------------

                return toResponse(
                                savedPayment,
                                treatment,
                                newTotalPaid,
                                newPending);
        }

        // =========================================================
        // GET PAYMENTS BY TREATMENT
        // =========================================================

        @Transactional
        public List<TreatmentPaymentResponse> findByTreatmentId(
                        String treatmentId) {

                Treatment treatment = treatmentRepository
                                .findById(treatmentId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el tratamiento "
                                                                                + treatmentId));

                BigDecimal cost = safe(treatment.getCost());

                BigDecimal totalPaid = safe(treatment.getAmountPaid());

                BigDecimal pending = cost.subtract(totalPaid);

                if (pending.compareTo(BigDecimal.ZERO) < 0) {
                        pending = BigDecimal.ZERO;
                }

                final BigDecimal finalPending = pending;

                List<TreatmentPayment> payments = paymentRepository
                                .findByTreatment_IdOrderByPaymentDateDesc(
                                                treatmentId);

                return payments
                                .stream()
                                .map(
                                                payment -> toResponse(
                                                                payment,
                                                                treatment,
                                                                totalPaid,
                                                                finalPending))
                                .toList();
        }

        // =========================================================
        // GET PAYMENTS BY PATIENT
        // =========================================================

        @Transactional
        public List<TreatmentPaymentResponse> findByPatientId(
                        String patientId) {

                List<TreatmentPayment> payments = paymentRepository
                                .findByTreatment_Patient_IdOrderByPaymentDateDesc(
                                                patientId);

                return payments
                                .stream()
                                .map(
                                                payment -> {

                                                        Treatment treatment = payment.getTreatment();

                                                        BigDecimal cost = safe(
                                                                        treatment.getCost());

                                                        BigDecimal totalPaid = safe(
                                                                        treatment.getAmountPaid());

                                                        BigDecimal pending = cost.subtract(totalPaid);

                                                        if (pending.compareTo(
                                                                        BigDecimal.ZERO) < 0) {
                                                                pending = BigDecimal.ZERO;
                                                        }

                                                        return toResponse(
                                                                        payment,
                                                                        treatment,
                                                                        totalPaid,
                                                                        pending);
                                                })
                                .toList();
        }

        // =========================================================
        // MAPPER
        // =========================================================

        private TreatmentPaymentResponse toResponse(
                        TreatmentPayment payment,
                        Treatment treatment,
                        BigDecimal totalPaid,
                        BigDecimal pendingAmount) {

                return new TreatmentPaymentResponse(
                                payment.getId(),

                                treatment.getId(),

                                treatment.getTreatmentName(),

                                treatment.getPatient().getId(),

                                payment.getAmount(),

                                payment.getPaymentDate(),

                                payment.getPaymentMethod(),

                                payment.getReference(),

                                payment.getNotes(),

                                safe(treatment.getCost()),

                                totalPaid,

                                pendingAmount);
        }

        // =========================================================
        // HELPERS
        // =========================================================

        private BigDecimal safe(
                        BigDecimal value) {
                return value != null
                                ? value
                                : BigDecimal.ZERO;
        }

        private String nullable(
                        String value) {
                if (value == null) {
                        return null;
                }

                final String trimmed = value.trim();

                return trimmed.isEmpty()
                                ? null
                                : trimmed;
        }

        @Transactional
        public PatientFinancialSummaryResponse getFinancialSummary(
                        String patientId) {

                System.out.println(
                                "========== FINANCIAL SUMMARY ==========");

                System.out.println(
                                "Patient ID: " + patientId);

                // =========================================================
                // 1. OBTENER TRATAMIENTOS
                // =========================================================

                List<Treatment> treatments = treatmentRepository.findByPatient_Id(
                                patientId);

                System.out.println(
                                "Treatments found: "
                                                + treatments.size());

                // =========================================================
                // 2. TOTALES
                // =========================================================

                BigDecimal totalTreatmentCost = BigDecimal.ZERO;

                BigDecimal totalPaid = BigDecimal.ZERO;

                BigDecimal totalPending = BigDecimal.ZERO;

                int treatmentsWithDebt = 0;

                List<TreatmentDebtResponse> treatmentSummaries = new ArrayList<>();

                // =========================================================
                // 3. RECORRER TRATAMIENTOS
                // =========================================================

                for (Treatment treatment : treatments) {

                        System.out.println(
                                        "Treatment: "
                                                        + treatment.getId()
                                                        + " - "
                                                        + treatment.getTreatmentName());

                        BigDecimal cost = treatment.getCost() != null
                                        ? treatment.getCost()
                                        : BigDecimal.ZERO;

                        BigDecimal amountPaid = treatment.getAmountPaid() != null
                                        ? treatment.getAmountPaid()
                                        : BigDecimal.ZERO;

                        BigDecimal pending = cost.subtract(amountPaid);

                        if (pending.compareTo(
                                        BigDecimal.ZERO) < 0) {
                                pending = BigDecimal.ZERO;
                        }

                        // =====================================================
                        // ACUMULAR TOTALES
                        // =====================================================

                        totalTreatmentCost = totalTreatmentCost.add(
                                        cost);

                        totalPaid = totalPaid.add(
                                        amountPaid);

                        totalPending = totalPending.add(
                                        pending);

                        // =====================================================
                        // OBTENER DIENTE
                        // =====================================================

                        String toothNumber = null;

                        if (treatment.getPatientTooth() != null) {

                                toothNumber = treatment
                                                .getPatientTooth()
                                                .getToothNumber();
                        }

                        // =====================================================
                        // AGREGAR ADEUDO
                        // =====================================================

                        if (pending.compareTo(BigDecimal.ZERO) > 0) {
                                treatmentsWithDebt++;
                        }

                        if (treatment.getPatientTooth() != null) {
                                toothNumber = treatment
                                                .getPatientTooth()
                                                .getToothNumber();
                        }

                        treatmentSummaries.add(
                                        new TreatmentDebtResponse(
                                                        treatment.getId(),
                                                        treatment.getTreatmentName(),
                                                        toothNumber,
                                                        cost,
                                                        amountPaid,
                                                        pending,
                                                        treatment.getStatus() != null
                                                                        ? treatment.getStatus().name()
                                                                        : null));
                }

                // =========================================================
                // 4. RESPONSE
                // =========================================================

                PatientFinancialSummaryResponse response = new PatientFinancialSummaryResponse(
                                patientId,
                                totalTreatmentCost,
                                totalPaid,
                                totalPending,
                                treatmentsWithDebt,
                                treatmentSummaries);

                System.out.println("Total cost: " + totalTreatmentCost);
                System.out.println("Total paid: " + totalPaid);
                System.out.println("Total pending: " + totalPending);
                System.out.println("Debts: " + treatmentSummaries.size());
                System.out.println("=======================================");

                return response;
        }
}