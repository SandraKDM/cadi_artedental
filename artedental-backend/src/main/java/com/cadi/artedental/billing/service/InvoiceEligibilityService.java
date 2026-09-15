package com.cadi.artedental.billing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadi.artedental.billing.model.InvoiceItemType;
import com.cadi.artedental.billing.model.InvoiceRequestStatus;
import com.cadi.artedental.billing.repository.InvoiceRequestRepository;

import com.cadi.artedental.treatment.model.Treatment;
import com.cadi.artedental.treatment.repository.TreatmentRepository;

import com.cadi.artedental.treatment.payment.model.TreatmentPayment;
import com.cadi.artedental.treatment.payment.repository.TreatmentPaymentRepository;

@Service
public class InvoiceEligibilityService {

        private final InvoiceRequestRepository invoiceRequestRepository;

        private final TreatmentRepository treatmentRepository;

        private final TreatmentPaymentRepository treatmentPaymentRepository;

        // =========================================================
        // CONSTRUCTOR
        // =========================================================

        public InvoiceEligibilityService(
                        InvoiceRequestRepository invoiceRequestRepository,
                        TreatmentRepository treatmentRepository,
                        TreatmentPaymentRepository treatmentPaymentRepository) {
                this.invoiceRequestRepository = invoiceRequestRepository;

                this.treatmentRepository = treatmentRepository;

                this.treatmentPaymentRepository = treatmentPaymentRepository;
        }

        // =========================================================
        // ACTIVE REQUEST
        // =========================================================

        public boolean hasActiveRequest(
                        InvoiceItemType itemType,
                        String referenceId) {

                boolean exists = invoiceRequestRepository
                                .existsByItemTypeAndReferenceIdAndStatusNot(
                                                itemType,
                                                referenceId,
                                                InvoiceRequestStatus.CANCELLED);

                System.out.println("========== INVOICE ELIGIBILITY ==========");
                System.out.println("TYPE: " + itemType);
                System.out.println("REFERENCE ID: [" + referenceId + "]");
                System.out.println("HAS ACTIVE REQUEST: " + exists);
                System.out.println("=========================================");

                return exists;
        }

        // =========================================================
        // CAN INVOICE TREATMENT
        // =========================================================

        @Transactional(readOnly = true)
        public boolean canInvoiceTreatment(
                        Treatment treatment) {

                String treatmentId = treatment.getId();

                // -----------------------------------------------------
                // 1. El tratamiento completo ya tiene solicitud
                // -----------------------------------------------------

                if (hasActiveRequest(
                                InvoiceItemType.TREATMENT,
                                treatmentId)) {
                        return false;
                }

                // -----------------------------------------------------
                // 2. Obtener pagos del tratamiento
                // -----------------------------------------------------

                List<TreatmentPayment> payments = treatmentPaymentRepository
                                .findByTreatment_IdOrderByPaymentDateDesc(
                                                treatmentId);

                // -----------------------------------------------------
                // 3. Si alguno de sus pagos ya fue solicitado,
                // no puede facturarse el tratamiento completo.
                // -----------------------------------------------------

                boolean hasRequestedPayment = payments.stream()
                                .anyMatch(
                                                payment -> hasActiveRequest(
                                                                InvoiceItemType.TREATMENT_PAYMENT,
                                                                payment
                                                                                .getId()
                                                                                .toString()));

                return !hasRequestedPayment;
        }

        // =========================================================
        // CAN INVOICE PAYMENT
        // =========================================================

        @Transactional(readOnly = true)
        public boolean canInvoicePayment(
                        Treatment treatment,
                        TreatmentPayment payment) {

                // -----------------------------------------------------
                // 1. El pago ya tiene solicitud
                // -----------------------------------------------------

                if (hasActiveRequest(
                                InvoiceItemType.TREATMENT_PAYMENT,
                                payment
                                                .getId()
                                                .toString())) {
                        return false;
                }

                // -----------------------------------------------------
                // 2. El tratamiento completo ya fue solicitado
                // -----------------------------------------------------

                if (hasActiveRequest(
                                InvoiceItemType.TREATMENT,
                                treatment.getId())) {
                        return false;
                }

                return true;
        }

        // =========================================================
        // VALIDATE
        // =========================================================

        @Transactional(readOnly = true)
        public void validate(
                        String patientId,
                        InvoiceItemType itemType,
                        String referenceId) {

                if (patientId == null ||
                                patientId.isBlank()) {

                        throw new IllegalArgumentException(
                                        "El paciente es obligatorio.");
                }

                if (itemType == null) {

                        throw new IllegalArgumentException(
                                        "El tipo de concepto es obligatorio.");
                }

                if (referenceId == null ||
                                referenceId.isBlank()) {

                        throw new IllegalArgumentException(
                                        "La referencia del concepto es obligatoria.");
                }

                switch (itemType) {

                        case TREATMENT ->
                                validateTreatment(
                                                patientId,
                                                referenceId);

                        case TREATMENT_PAYMENT ->
                                validateTreatmentPayment(
                                                patientId,
                                                referenceId);

                        case APPOINTMENT ->
                                validateAppointment(
                                                patientId,
                                                referenceId);
                }
        }

        // =========================================================
        // VALIDATE TREATMENT
        // =========================================================

        private void validateTreatment(
                        String patientId,
                        String treatmentId) {

                Treatment treatment = treatmentRepository
                                .findById(treatmentId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el tratamiento."));

                // -----------------------------------------------------
                // Validar paciente
                // -----------------------------------------------------

                if (treatment.getPatient() == null ||
                                treatment
                                                .getPatient()
                                                .getId() == null
                                ||
                                !treatment
                                                .getPatient()
                                                .getId()
                                                .equals(patientId)) {

                        throw new IllegalArgumentException(
                                        "El tratamiento no pertenece al paciente.");
                }

                // -----------------------------------------------------
                // Validar costo
                // -----------------------------------------------------

                BigDecimal cost = treatment.getCost() != null
                                ? treatment.getCost()
                                : BigDecimal.ZERO;

                if (cost.compareTo(
                                BigDecimal.ZERO) <= 0) {

                        throw new IllegalStateException(
                                        "El tratamiento no tiene un importe facturable.");
                }

                // -----------------------------------------------------
                // Validar elegibilidad
                // -----------------------------------------------------

                if (!canInvoiceTreatment(
                                treatment)) {

                        throw new IllegalStateException(
                                        "El tratamiento completo ya no está disponible "
                                                        + "para facturación porque tiene una solicitud "
                                                        + "activa o uno de sus pagos ya fue solicitado.");
                }
        }

        // =========================================================
        // VALIDATE TREATMENT PAYMENT
        // =========================================================

        private void validateTreatmentPayment(
                        String patientId,
                        String paymentId) {

                UUID paymentUuid;

                try {

                        paymentUuid = UUID.fromString(
                                        paymentId);

                } catch (IllegalArgumentException ex) {

                        throw new IllegalArgumentException(
                                        "El identificador del pago no es válido.");
                }

                TreatmentPayment payment = treatmentPaymentRepository
                                .findById(paymentUuid)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el pago."));

                Treatment treatment = payment.getTreatment();

                if (treatment == null) {

                        throw new IllegalStateException(
                                        "El pago no tiene un tratamiento asociado.");
                }

                // -----------------------------------------------------
                // Validar paciente
                // -----------------------------------------------------

                if (treatment.getPatient() == null ||
                                treatment
                                                .getPatient()
                                                .getId() == null
                                ||
                                !treatment
                                                .getPatient()
                                                .getId()
                                                .equals(patientId)) {

                        throw new IllegalArgumentException(
                                        "El pago no pertenece al paciente.");
                }

                // -----------------------------------------------------
                // Validar monto
                // -----------------------------------------------------

                BigDecimal amount = payment.getAmount() != null
                                ? payment.getAmount()
                                : BigDecimal.ZERO;

                if (amount.compareTo(
                                BigDecimal.ZERO) <= 0) {

                        throw new IllegalStateException(
                                        "El pago no tiene un importe facturable.");
                }

                // -----------------------------------------------------
                // Validar elegibilidad
                // -----------------------------------------------------

                if (!canInvoicePayment(
                                treatment,
                                payment)) {

                        throw new IllegalStateException(
                                        "Este pago ya no está disponible para "
                                                        + "facturación porque tiene una solicitud activa "
                                                        + "o el tratamiento completo ya fue solicitado.");
                }
        }

        // =========================================================
        // VALIDATE APPOINTMENT
        // =========================================================

        private void validateAppointment(
                        String patientId,
                        String appointmentId) {

                /*
                 * Todavía no hemos conectado Appointment
                 * al módulo de facturación.
                 *
                 * Se implementará cuando adaptemos el modelo
                 * real de citas y su costo.
                 */

                throw new UnsupportedOperationException(
                                "La facturación de citas todavía no está habilitada.");
        }
}