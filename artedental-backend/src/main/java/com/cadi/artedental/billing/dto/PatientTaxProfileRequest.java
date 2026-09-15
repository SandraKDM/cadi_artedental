package com.cadi.artedental.billing.dto;

public class PatientTaxProfileRequest {

    private String rfc;

    private String businessName;

    private String taxRegime;

    private String fiscalZipCode;

    private String cfdiUse;

    private String billingEmail;

    public PatientTaxProfileRequest() {
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(
        String businessName
    ) {
        this.businessName = businessName;
    }

    public String getTaxRegime() {
        return taxRegime;
    }

    public void setTaxRegime(
        String taxRegime
    ) {
        this.taxRegime = taxRegime;
    }

    public String getFiscalZipCode() {
        return fiscalZipCode;
    }

    public void setFiscalZipCode(
        String fiscalZipCode
    ) {
        this.fiscalZipCode = fiscalZipCode;
    }

    public String getCfdiUse() {
        return cfdiUse;
    }

    public void setCfdiUse(
        String cfdiUse
    ) {
        this.cfdiUse = cfdiUse;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(
        String billingEmail
    ) {
        this.billingEmail = billingEmail;
    }
}