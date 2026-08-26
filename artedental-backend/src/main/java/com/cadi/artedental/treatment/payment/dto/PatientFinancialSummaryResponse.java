package com.cadi.artedental.treatment.payment.dto;

import java.math.BigDecimal;
import java.util.List;

public record PatientFinancialSummaryResponse(
    String patientId,
    BigDecimal totalTreatmentCost,
    BigDecimal totalPaid,
    BigDecimal totalPending,
    int treatmentsWithDebt,
    List<TreatmentDebtResponse> treatments
) {
}