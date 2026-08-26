package com.cadi.artedental.treatment.payment.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TreatmentPaymentRequest(

    BigDecimal amount,

    OffsetDateTime paymentDate,

    String paymentMethod,

    String reference,

    String notes

) {
}