package com.cadi.artedental.treatment.payment.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TreatmentPaymentResponse(

    UUID id,

    String treatmentId,

    String treatmentName,

    String patientId,

    BigDecimal amount,

    OffsetDateTime paymentDate,

    String paymentMethod,

    String reference,

    String notes,

    BigDecimal treatmentCost,

    BigDecimal totalPaid,

    BigDecimal pendingAmount

) {
}