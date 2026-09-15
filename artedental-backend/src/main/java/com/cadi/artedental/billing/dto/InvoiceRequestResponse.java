package com.cadi.artedental.billing.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.cadi.artedental.billing.model.InvoiceItemType;
import com.cadi.artedental.billing.model.InvoiceRequestStatus;

public class InvoiceRequestResponse {

    private String id;

    private String patientId;

    private InvoiceItemType itemType;

    private String referenceId;

    private String description;

    private BigDecimal amount;

    private String rfc;

    private String businessName;

    private String taxRegime;

    private String fiscalZipCode;

    private String cfdiUse;

    private String billingEmail;

    private String accountantEmail;

    private InvoiceRequestStatus status;

    private OffsetDateTime requestedAt;

    private OffsetDateTime processedAt;

    private String notes;

    public InvoiceRequestResponse() {
    }

    public InvoiceRequestResponse(
        String id,
        String patientId,
        InvoiceItemType itemType,
        String referenceId,
        String description,
        BigDecimal amount,
        String rfc,
        String businessName,
        String taxRegime,
        String fiscalZipCode,
        String cfdiUse,
        String billingEmail,
        String accountantEmail,
        InvoiceRequestStatus status,
        OffsetDateTime requestedAt,
        OffsetDateTime processedAt,
        String notes
    ) {
        this.id = id;
        this.patientId = patientId;
        this.itemType = itemType;
        this.referenceId = referenceId;
        this.description = description;
        this.amount = amount;
        this.rfc = rfc;
        this.businessName = businessName;
        this.taxRegime = taxRegime;
        this.fiscalZipCode = fiscalZipCode;
        this.cfdiUse = cfdiUse;
        this.billingEmail = billingEmail;
        this.accountantEmail = accountantEmail;
        this.status = status;
        this.requestedAt = requestedAt;
        this.processedAt = processedAt;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
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

    public String getRfc() {
        return rfc;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getTaxRegime() {
        return taxRegime;
    }

    public String getFiscalZipCode() {
        return fiscalZipCode;
    }

    public String getCfdiUse() {
        return cfdiUse;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public String getAccountantEmail() {
        return accountantEmail;
    }

    public InvoiceRequestStatus getStatus() {
        return status;
    }

    public OffsetDateTime getRequestedAt() {
        return requestedAt;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }

    public String getNotes() {
        return notes;
    }
    
}