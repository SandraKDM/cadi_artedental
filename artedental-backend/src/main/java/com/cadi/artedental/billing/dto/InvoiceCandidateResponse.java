package com.cadi.artedental.billing.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.cadi.artedental.billing.model.InvoiceItemType;

public class InvoiceCandidateResponse {

    private InvoiceItemType itemType;

    private String referenceId;

    private String description;

    private BigDecimal amount;

    private OffsetDateTime date;

    private String treatmentId;

    private String treatmentName;

    private String toothNumber;

    public InvoiceCandidateResponse() {
    }

    public InvoiceCandidateResponse(
        InvoiceItemType itemType,
        String referenceId,
        String description,
        BigDecimal amount,
        OffsetDateTime date,
        String treatmentId,
        String treatmentName,
        String toothNumber
    ) {
        this.itemType = itemType;
        this.referenceId = referenceId;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.treatmentId = treatmentId;
        this.treatmentName = treatmentName;
        this.toothNumber = toothNumber;
    }

    public InvoiceItemType getItemType() {
        return itemType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public String getToothNumber() {
        return toothNumber;
    }
}
