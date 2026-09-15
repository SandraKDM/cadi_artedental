package com.cadi.artedental.billing.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadi.artedental.billing.dto.InvoiceCandidateResponse;
import com.cadi.artedental.billing.model.InvoiceItemType;
import com.cadi.artedental.billing.model.InvoiceRequest;
import com.cadi.artedental.billing.model.InvoiceRequestStatus;
import com.cadi.artedental.billing.repository.InvoiceRequestRepository;

import com.cadi.artedental.treatment.model.Treatment;
import com.cadi.artedental.treatment.repository.TreatmentRepository;

import com.cadi.artedental.treatment.payment.model.TreatmentPayment;
import com.cadi.artedental.treatment.payment.repository.TreatmentPaymentRepository;

@Service
public class InvoiceCandidateService {

        private final TreatmentRepository treatmentRepository;

        private final TreatmentPaymentRepository paymentRepository;

        private final InvoiceRequestRepository invoiceRequestRepository;

        private final InvoiceEligibilityService eligibilityService;

        public InvoiceCandidateService(
                        TreatmentRepository treatmentRepository,
                        TreatmentPaymentRepository paymentRepository,
                        InvoiceRequestRepository invoiceRequestRepository,
                        InvoiceEligibilityService eligibilityService) {
                this.treatmentRepository = treatmentRepository;

                this.paymentRepository = paymentRepository;

                this.invoiceRequestRepository = invoiceRequestRepository;

                this.eligibilityService = eligibilityService;
        }

        // =========================================================
        // GET CANDIDATES
        // =========================================================

        @Transactional(readOnly = true)
        public List<InvoiceCandidateResponse> getCandidates(
                        String patientId) {

                List<InvoiceCandidateResponse> candidates = new ArrayList<>();

                // =====================================================
                // 1. SOLICITUDES EXISTENTES
                // =====================================================

                System.out.println("========== GET INVOICE CANDIDATES ==========");

                System.out.println("PATIENT ID: " + patientId);

                List<InvoiceRequest> existingRequests = invoiceRequestRepository
                                .findByPatient_IdAndStatusNot(
                                                patientId,
                                                InvoiceRequestStatus.CANCELLED);

                Set<String> invoicedTreatments = new HashSet<>();

                Set<String> invoicedPayments = new HashSet<>();

                for (InvoiceRequest request : existingRequests) {

                        if (request.getItemType() == InvoiceItemType.TREATMENT) {

                                invoicedTreatments.add(
                                                request.getReferenceId());
                        }

                        if (request.getItemType() == InvoiceItemType.TREATMENT_PAYMENT) {

                                invoicedPayments.add(
                                                request.getReferenceId());
                        }
                }

                // =====================================================
                // 2. TRATAMIENTOS
                // =====================================================

                List<Treatment> treatments = treatmentRepository
                                .findByPatient_Id(
                                                patientId);

                for (Treatment treatment : treatments) {

                        addTreatmentCandidates(
                                        treatment,
                                        candidates,
                                        invoicedTreatments,
                                        invoicedPayments);
                }

                return candidates;
        }

        // =========================================================
        // TREATMENT + PAYMENTS
        // =========================================================

        private void addTreatmentCandidates(
                        Treatment treatment,
                        List<InvoiceCandidateResponse> candidates,
                        Set<String> invoicedTreatments,
                        Set<String> invoicedPayments) {

                String treatmentId = treatment.getId();

                BigDecimal treatmentCost = treatment.getCost() != null
                                ? treatment.getCost()
                                : BigDecimal.ZERO;

                // =====================================================
                // OBTENER PAGOS
                // =====================================================

                List<TreatmentPayment> payments = paymentRepository
                                .findByTreatment_IdOrderByPaymentDateDesc(
                                                treatmentId);

                // =====================================================
                // CANDIDATO: TRATAMIENTO COMPLETO
                // =====================================================

                if (treatmentCost.compareTo(
                                BigDecimal.ZERO) > 0) {

                        boolean treatmentEligible = eligibilityService
                                        .canInvoiceTreatment(
                                                        treatment);

                        System.out.println(
                                        "TREATMENT CANDIDATE -> "
                                                        + "id=[" + treatmentId + "]"
                                                        + ", eligible="
                                                        + treatmentEligible);

                        if (treatmentEligible) {

                                String toothNumber = getToothNumber(
                                                treatment);

                                String description = buildTreatmentDescription(
                                                treatment,
                                                toothNumber);

                                candidates.add(
                                                new InvoiceCandidateResponse(
                                                                InvoiceItemType.TREATMENT,
                                                                treatmentId,
                                                                description,
                                                                treatmentCost,
                                                                treatment.getStartDate(),
                                                                treatmentId,
                                                                treatment.getTreatmentName(),
                                                                toothNumber));
                        }
                }

                // =====================================================
                // CANDIDATOS: PAGOS
                // =====================================================

                for (TreatmentPayment payment : payments) {

                        BigDecimal amount = payment.getAmount() != null
                                        ? payment.getAmount()
                                        : BigDecimal.ZERO;

                        if (amount.compareTo(
                                        BigDecimal.ZERO) <= 0) {
                                continue;
                        }

                        // -------------------------------------------------
                        // Validar elegibilidad del pago
                        // -------------------------------------------------

                        boolean paymentEligible = eligibilityService
                                        .canInvoicePayment(
                                                        treatment,
                                                        payment);

                        System.out.println(
                                        "PAYMENT CANDIDATE -> "
                                                        + "id=[" + payment.getId() + "]"
                                                        + ", amount=" + amount
                                                        + ", eligible="
                                                        + paymentEligible);

                        if (!paymentEligible) {
                                continue;
                        }

                        String toothNumber = getToothNumber(
                                        treatment);

                        String description = buildPaymentDescription(
                                        treatment,
                                        toothNumber);

                        String referenceId = payment
                                        .getId()
                                        .toString();

                        candidates.add(
                                        new InvoiceCandidateResponse(
                                                        InvoiceItemType.TREATMENT_PAYMENT,
                                                        referenceId,
                                                        description,
                                                        amount,
                                                        payment.getPaymentDate(),
                                                        treatmentId,
                                                        treatment.getTreatmentName(),
                                                        toothNumber));
                }
        }

        // =========================================================
        // TOOTH
        // =========================================================

        private String getToothNumber(
                        Treatment treatment) {

                if (treatment.getPatientTooth() == null) {
                        return null;
                }

                return treatment
                                .getPatientTooth()
                                .getToothNumber();
        }

        // =========================================================
        // DESCRIPTION
        // =========================================================

        private String buildTreatmentDescription(
                        Treatment treatment,
                        String toothNumber) {

                String name = treatment.getTreatmentName();

                if (toothNumber == null ||
                                toothNumber.isBlank()) {
                        return name;
                }

                return name
                                + " - Diente "
                                + toothNumber;
        }

        private String buildPaymentDescription(
                        Treatment treatment,
                        String toothNumber) {

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

                return description.toString();
        }
}