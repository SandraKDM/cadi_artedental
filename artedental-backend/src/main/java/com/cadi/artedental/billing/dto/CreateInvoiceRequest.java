package com.cadi.artedental.billing.dto;

import com.cadi.artedental.billing.model.InvoiceItemType;

public class CreateInvoiceRequest {

    private InvoiceItemType itemType;

    private String referenceId;

    private String accountantEmail;

    private String notes;

    public CreateInvoiceRequest() {
    }

    public InvoiceItemType getItemType() {
        return itemType;
    }

    public void setItemType(
        InvoiceItemType itemType
    ) {
        this.itemType = itemType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(
        String referenceId
    ) {
        this.referenceId = referenceId;
    }

    public String getAccountantEmail() {
        return accountantEmail;
    }

    public void setAccountantEmail(
        String accountantEmail
    ) {
        this.accountantEmail =
            accountantEmail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(
        String notes
    ) {
        this.notes = notes;
    }
}