package com.cadi.artedental.billing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadi.artedental.billing.dto.CreateInvoiceRequest;
import com.cadi.artedental.billing.dto.InvoiceRequestResponse;
import com.cadi.artedental.billing.model.InvoiceItemType;
import com.cadi.artedental.billing.model.InvoiceRequest;
import com.cadi.artedental.billing.model.InvoiceRequestStatus;
import com.cadi.artedental.billing.model.PatientTaxProfile;
import com.cadi.artedental.billing.repository.InvoiceRequestRepository;
import com.cadi.artedental.billing.repository.PatientTaxProfileRepository;

import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.patient.repository.PatientRepository;

import com.cadi.artedental.treatment.model.Treatment;
import com.cadi.artedental.treatment.repository.TreatmentRepository;

import com.cadi.artedental.treatment.payment.model.TreatmentPayment;
import com.cadi.artedental.treatment.payment.repository.TreatmentPaymentRepository;

@Service
public class InvoiceRequestService {

        private final InvoiceRequestRepository invoiceRequestRepository;

        private final PatientTaxProfileRepository taxProfileRepository;

        private final PatientRepository patientRepository;

        private final TreatmentRepository treatmentRepository;

        private final TreatmentPaymentRepository paymentRepository;

        private final InvoiceEligibilityService eligibilityService;

        private final BillingEmailService billingEmailService;

        public InvoiceRequestService(
                        InvoiceRequestRepository invoiceRequestRepository,
                        PatientTaxProfileRepository taxProfileRepository,
                        PatientRepository patientRepository,
                        TreatmentRepository treatmentRepository,
                        TreatmentPaymentRepository paymentRepository,
                        InvoiceEligibilityService eligibilityService,
                        BillingEmailService billingEmailService) {
                this.invoiceRequestRepository = invoiceRequestRepository;

                this.taxProfileRepository = taxProfileRepository;

                this.patientRepository = patientRepository;

                this.treatmentRepository = treatmentRepository;

                this.paymentRepository = paymentRepository;

                this.eligibilityService = eligibilityService;

                this.billingEmailService = billingEmailService;
        }

        // =========================================================
        // CREATE
        // =========================================================

        @Transactional
        public InvoiceRequestResponse create(
                        String patientId,
                        CreateInvoiceRequest request) {

                // -----------------------------------------------------
                // 1. VALIDAR REQUEST
                // -----------------------------------------------------

                if (request == null) {
                        throw new IllegalArgumentException(
                                        "La solicitud de facturación es obligatoria.");
                }

                if (request.getItemType() == null) {
                        throw new IllegalArgumentException(
                                        "El tipo de concepto es obligatorio.");
                }

                String referenceId = normalizeRequired(
                                request.getReferenceId(),
                                "La referencia del concepto es obligatoria.");

                System.out.println(
                                "ACCOUNTANT EMAIL RECIBIDO: [" +
                                                request.getAccountantEmail() +
                                                "]");

                String accountantEmail = normalizeEmail(
                                request.getAccountantEmail());

                // -----------------------------------------------------
                // 2. BUSCAR PACIENTE
                // -----------------------------------------------------

                Patient patient = patientRepository
                                .findById(patientId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el paciente "
                                                                                + patientId));

                // -----------------------------------------------------
                // 3. OBTENER DATOS FISCALES
                // -----------------------------------------------------

                PatientTaxProfile taxProfile = taxProfileRepository
                                .findByPatient_Id(patientId)
                                .orElseThrow(
                                                () -> new IllegalStateException(
                                                                "El paciente debe registrar sus datos "
                                                                                + "fiscales antes de solicitar una factura."));

                // -----------------------------------------------------
                // 4. VALIDAR ELEGIBILIDAD
                // -----------------------------------------------------

                eligibilityService.validate(
                                patientId,
                                request.getItemType(),
                                referenceId);

                // -----------------------------------------------------
                // 5. RESOLVER CONCEPTO REAL
                // -----------------------------------------------------

                ResolvedInvoiceItem item = resolveItem(
                                patientId,
                                request.getItemType(),
                                referenceId);

                if (item.amount() == null ||
                                item.amount()
                                                .compareTo(BigDecimal.ZERO) <= 0) {

                        throw new IllegalStateException(
                                        "El concepto seleccionado no tiene "
                                                        + "un importe facturable.");
                }

                // -----------------------------------------------------
                // 6. CREAR SOLICITUD
                // -----------------------------------------------------

                InvoiceRequest invoiceRequest = new InvoiceRequest();

                invoiceRequest.setPatient(
                                patient);

                invoiceRequest.setItemType(
                                request.getItemType());

                invoiceRequest.setReferenceId(
                                referenceId);

                invoiceRequest.setDescription(
                                item.description());

                invoiceRequest.setAmount(
                                item.amount());

                // -----------------------------------------------------
                // SNAPSHOT FISCAL
                // -----------------------------------------------------

                invoiceRequest.setRfc(
                                taxProfile.getRfc());

                invoiceRequest.setBusinessName(
                                taxProfile.getBusinessName());

                invoiceRequest.setTaxRegime(
                                taxProfile.getTaxRegime());

                invoiceRequest.setFiscalZipCode(
                                taxProfile.getFiscalZipCode());

                invoiceRequest.setCfdiUse(
                                taxProfile.getCfdiUse());

                invoiceRequest.setBillingEmail(
                                taxProfile.getBillingEmail());

                // -----------------------------------------------------
                // CONTADOR
                // -----------------------------------------------------

                invoiceRequest.setAccountantEmail(
                                accountantEmail);

                invoiceRequest.setStatus(
                                InvoiceRequestStatus.PENDING);

                invoiceRequest.setNotes(
                                nullable(
                                                request.getNotes()));

                // -----------------------------------------------------
                // 7. GUARDAR
                // -----------------------------------------------------

                try {

                        InvoiceRequest saved;

                        try {

                                saved = invoiceRequestRepository
                                                .saveAndFlush(
                                                                invoiceRequest);

                        } catch (DataIntegrityViolationException ex) {

                                throw new IllegalStateException(
                                                "Este concepto ya tiene una solicitud "
                                                                + "de facturación activa.");
                        }

                        // =========================================================
                        // ENVIAR CORREO
                        // =========================================================

                        try {

                                billingEmailService
                                                .sendInvoiceRequest(
                                                                saved);

                                saved.setStatus(
                                                InvoiceRequestStatus.SENT);

                                saved = invoiceRequestRepository.save(
                                                saved);

                        } catch (Exception ex) {

                                /*
                                 * No eliminamos la solicitud.
                                 *
                                 * Se conserva PENDING para poder
                                 * reintentar posteriormente.
                                 */

                                System.err.println(
                                                "Error enviando solicitud de facturación "
                                                                + saved.getId()
                                                                + ": "
                                                                + ex.getMessage());
                        }

                        return toResponse(saved);

                } catch (DataIntegrityViolationException ex) {

                        /*
                         * Protege también contra el índice único parcial
                         * uq_invoice_requests_active_item.
                         */

                        throw new IllegalStateException(
                                        "Este concepto ya tiene una solicitud "
                                                        + "de facturación activa.");
                }
        }

        // =========================================================
        // FIND BY PATIENT
        // =========================================================

        @Transactional(readOnly = true)
        public List<InvoiceRequestResponse> findByPatientId(
                        String patientId) {

                return invoiceRequestRepository
                                .findByPatient_IdOrderByRequestedAtDesc(
                                                patientId)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // =========================================================
        // RESOLVE ITEM
        // =========================================================

        private ResolvedInvoiceItem resolveItem(
                        String patientId,
                        InvoiceItemType itemType,
                        String referenceId) {

                return switch (itemType) {

                        case TREATMENT ->
                                resolveTreatment(
                                                patientId,
                                                referenceId);

                        case TREATMENT_PAYMENT ->
                                resolveTreatmentPayment(
                                                patientId,
                                                referenceId);

                        case APPOINTMENT ->
                                throw new UnsupportedOperationException(
                                                "La facturación de citas todavía "
                                                                + "no está habilitada.");
                };
        }

        // =========================================================
        // RESOLVE TREATMENT
        // =========================================================

        private ResolvedInvoiceItem resolveTreatment(
                        String patientId,
                        String treatmentId) {

                Treatment treatment = treatmentRepository
                                .findById(treatmentId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el tratamiento."));

                validateTreatmentPatient(
                                treatment,
                                patientId);

                BigDecimal amount = treatment.getCost() != null
                                ? treatment.getCost()
                                : BigDecimal.ZERO;

                String toothNumber = null;

                if (treatment.getPatientTooth() != null) {

                        toothNumber = treatment
                                        .getPatientTooth()
                                        .getToothNumber();
                }

                String description = treatment.getTreatmentName();

                if (toothNumber != null &&
                                !toothNumber.isBlank()) {

                        description += " - Diente "
                                        + toothNumber;
                }

                return new ResolvedInvoiceItem(
                                description,
                                amount);
        }

        // =========================================================
        // RESOLVE PAYMENT
        // =========================================================

        private ResolvedInvoiceItem resolveTreatmentPayment(
                        String patientId,
                        String paymentId) {

                System.out.println(
                                "PAYMENT REFERENCE ID RECIBIDO: ["
                                                + paymentId
                                                + "]");

                UUID paymentUuid;

                try {

                        paymentUuid = UUID.fromString(
                                        paymentId);

                } catch (IllegalArgumentException ex) {

                        throw new IllegalArgumentException(
                                        "El identificador del pago no es válido.");
                }

                TreatmentPayment payment = paymentRepository
                                .findById(paymentUuid)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró el pago."));

                Treatment treatment = payment.getTreatment();

                if (treatment == null) {

                        throw new IllegalStateException(
                                        "El pago no tiene un tratamiento asociado.");
                }

                validateTreatmentPatient(
                                treatment,
                                patientId);

                BigDecimal amount = payment.getAmount() != null
                                ? payment.getAmount()
                                : BigDecimal.ZERO;

                String toothNumber = null;

                if (treatment.getPatientTooth() != null) {

                        toothNumber = treatment
                                        .getPatientTooth()
                                        .getToothNumber();
                }

                StringBuilder description = new StringBuilder();

                description.append(
                                "Pago - ");

                description.append(
                                treatment.getTreatmentName());

                if (toothNumber != null &&
                                !toothNumber.isBlank()) {

                        description.append(
                                        " - Diente ");

                        description.append(
                                        toothNumber);
                }

                return new ResolvedInvoiceItem(
                                description.toString(),
                                amount);
        }

        // =========================================================
        // PATIENT VALIDATION
        // =========================================================

        private void validateTreatmentPatient(
                        Treatment treatment,
                        String patientId) {

                if (treatment.getPatient() == null ||
                                treatment.getPatient().getId() == null ||
                                !treatment
                                                .getPatient()
                                                .getId()
                                                .equals(patientId)) {

                        throw new IllegalArgumentException(
                                        "El concepto seleccionado no pertenece "
                                                        + "al paciente.");
                }
        }

        // =========================================================
        // RESPONSE
        // =========================================================

        private InvoiceRequestResponse toResponse(
                        InvoiceRequest request) {

                String patientId = request.getPatient() != null
                                ? request
                                                .getPatient()
                                                .getId()
                                : null;

                return new InvoiceRequestResponse(
                                request.getId(),

                                patientId,

                                request.getItemType(),

                                request.getReferenceId(),

                                request.getDescription(),

                                request.getAmount(),

                                request.getRfc(),

                                request.getBusinessName(),

                                request.getTaxRegime(),

                                request.getFiscalZipCode(),

                                request.getCfdiUse(),

                                request.getBillingEmail(),

                                request.getAccountantEmail(),

                                request.getStatus(),

                                request.getRequestedAt(),

                                request.getProcessedAt(),

                                request.getNotes());
        }

        @Transactional
        public InvoiceRequestResponse retrySend(
                        String patientId,
                        String invoiceRequestId) {

                InvoiceRequest request = findRequestForPatient(
                                patientId,
                                invoiceRequestId);

                if (request.getStatus() != InvoiceRequestStatus.PENDING) {

                        throw new IllegalStateException(
                                        "Solo se pueden reenviar solicitudes pendientes.");
                }

                billingEmailService
                                .sendInvoiceRequest(
                                                request);

                request.setStatus(
                                InvoiceRequestStatus.SENT);

                InvoiceRequest saved = invoiceRequestRepository.save(
                                request);

                return toResponse(saved);
        }

        @Transactional
        public InvoiceRequestResponse markAsProcessed(
                        String patientId,
                        String invoiceRequestId) {

                InvoiceRequest request = findRequestForPatient(
                                patientId,
                                invoiceRequestId);

                if (request.getStatus() != InvoiceRequestStatus.SENT) {
                        throw new IllegalStateException(
                                        "Solo una solicitud enviada puede "
                                                        + "marcarse como procesada.");
                }

                request.setStatus(
                                InvoiceRequestStatus.PROCESSED);

                request.setProcessedAt(
                                java.time.OffsetDateTime.now());

                InvoiceRequest saved = invoiceRequestRepository.save(
                                request);

                return toResponse(saved);
        }

        @Transactional
        public InvoiceRequestResponse cancel(
                        String patientId,
                        String invoiceRequestId) {

                InvoiceRequest request = findRequestForPatient(
                                patientId,
                                invoiceRequestId);

                if (request.getStatus() == InvoiceRequestStatus.PROCESSED) {
                        throw new IllegalStateException(
                                        "Una solicitud procesada no puede cancelarse.");
                }

                if (request.getStatus() == InvoiceRequestStatus.CANCELLED) {
                        throw new IllegalStateException(
                                        "La solicitud ya se encuentra cancelada.");
                }

                request.setStatus(
                                InvoiceRequestStatus.CANCELLED);

                InvoiceRequest saved = invoiceRequestRepository.save(
                                request);

                return toResponse(saved);
        }

        // =========================================================
        // HELPERS
        // =========================================================

        private String normalizeRequired(
                        String value,
                        String message) {

                if (value == null ||
                                value.trim().isEmpty()) {

                        throw new IllegalArgumentException(
                                        message);
                }

                return value.trim();
        }

        private String normalizeEmail(
                        String value) {

                String email = normalizeRequired(
                                value,
                                "El correo del contador es obligatorio.")
                                .trim()
                                .toLowerCase(Locale.ROOT);

                if (!email.matches(
                                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

                        throw new IllegalArgumentException(
                                        "El correo del contador no tiene "
                                                        + "un formato válido.");
                }

                return email;
        }

        private String nullable(
                        String value) {

                if (value == null) {
                        return null;
                }

                String trimmed = value.trim();

                return trimmed.isEmpty()
                                ? null
                                : trimmed;
        }

        private InvoiceRequest findRequestForPatient(
                        String patientId,
                        String invoiceRequestId) {

                InvoiceRequest request = invoiceRequestRepository
                                .findById(invoiceRequestId)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "No se encontró la solicitud "
                                                                                + "de facturación."));

                if (request.getPatient() == null ||
                                request.getPatient().getId() == null ||
                                !request
                                                .getPatient()
                                                .getId()
                                                .equals(patientId)) {

                        throw new IllegalArgumentException(
                                        "La solicitud de facturación no "
                                                        + "pertenece al paciente.");
                }

                return request;
        }
}