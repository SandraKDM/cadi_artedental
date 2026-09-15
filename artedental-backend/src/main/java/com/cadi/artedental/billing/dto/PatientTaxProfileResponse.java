package com.cadi.artedental.billing.dto;

import java.time.OffsetDateTime;

public class PatientTaxProfileResponse {

    private String id;

    private String patientId;

    private String rfc;

    private String businessName;

    private String taxRegime;

    private String fiscalZipCode;

    private String cfdiUse;

    private String billingEmail;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public PatientTaxProfileResponse() {
    }

    public PatientTaxProfileResponse(
        String id,
        String patientId,
        String rfc,
        String businessName,
        String taxRegime,
        String fiscalZipCode,
        String cfdiUse,
        String billingEmail,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
        this.id = id;
        this.patientId = patientId;
        this.rfc = rfc;
        this.businessName = businessName;
        this.taxRegime = taxRegime;
        this.fiscalZipCode = fiscalZipCode;
        this.cfdiUse = cfdiUse;
        this.billingEmail = billingEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}