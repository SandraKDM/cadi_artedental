package com.cadi.artedental.treatment.payment.dto;

import java.math.BigDecimal;

public record TreatmentDebtResponse(

    String treatmentId,

    String treatmentName,

    String toothNumber,

    BigDecimal cost,

    BigDecimal amountPaid,

    BigDecimal pendingAmount,

    String status

) {
}