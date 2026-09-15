package com.cadi.artedental.billing.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.cadi.artedental.patient.model.Patient;

import jakarta.persistence.*;

@Entity
@Table(
    name = "invoice_requests"
)
public class InvoiceRequest {

    @Id
    @Column(
        name = "id",
        nullable = false,
        length = 36
    )
    private String id;

    // =========================================================
    // PATIENT
    // =========================================================

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "patient_id",
        nullable = false
    )
    private Patient patient;

    // =========================================================
    // ITEM
    // =========================================================

    @Enumerated(EnumType.STRING)
    @Column(
        name = "item_type",
        nullable = false,
        length = 30
    )
    private InvoiceItemType itemType;

    @Column(
        name = "reference_id",
        nullable = false,
        length = 255
    )
    private String referenceId;

    @Column(
        name = "description",
        nullable = false,
        length = 500
    )
    private String description;

    @Column(
        name = "amount",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal amount;

    // =========================================================
    // TAX SNAPSHOT
    // =========================================================

    @Column(
        name = "rfc",
        nullable = false,
        length = 13
    )
    private String rfc;

    @Column(
        name = "business_name",
        nullable = false,
        length = 255
    )
    private String businessName;

    @Column(
        name = "tax_regime",
        nullable = false,
        length = 10
    )
    private String taxRegime;

    @Column(
        name = "fiscal_zip_code",
        nullable = false,
        length = 5
    )
    private String fiscalZipCode;

    @Column(
        name = "cfdi_use",
        nullable = false,
        length = 10
    )
    private String cfdiUse;

    @Column(
        name = "billing_email",
        nullable = false,
        length = 255
    )
    private String billingEmail;

    // =========================================================
    // ACCOUNTANT
    // =========================================================

    @Column(
        name = "accountant_email",
        nullable = false,
        length = 255
    )
    private String accountantEmail;

    // =========================================================
    // STATUS
    // =========================================================

    @Enumerated(EnumType.STRING)
    @Column(
        name = "status",
        nullable = false,
        length = 30
    )
    private InvoiceRequestStatus status;

    // =========================================================
    // DATES
    // =========================================================

    @Column(
        name = "requested_at",
        nullable = false
    )
    private OffsetDateTime requestedAt;

    @Column(
        name = "processed_at"
    )
    private OffsetDateTime processedAt;

    // =========================================================
    // NOTES
    // =========================================================

    @Column(
        name = "notes",
        columnDefinition = "TEXT"
    )
    private String notes;

    // =========================================================
    // AUDIT
    // =========================================================

    @Column(
        name = "created_at",
        nullable = false
    )
    private OffsetDateTime createdAt;

    @Column(
        name = "updated_at",
        nullable = false
    )
    private OffsetDateTime updatedAt;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public InvoiceRequest() {
    }

    // =========================================================
    // CALLBACKS
    // =========================================================

    @PrePersist
    protected void onCreate() {

        if (id == null) {
            id =
                UUID.randomUUID()
                    .toString();
        }

        OffsetDateTime now =
            OffsetDateTime.now();

        if (status == null) {
            status =
                InvoiceRequestStatus.PENDING;
        }

        if (requestedAt == null) {
            requestedAt = now;
        }

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt =
            OffsetDateTime.now();
    }

    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

    public String getId() {
        return id;
    }

    public void setId(
        String id
    ) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(
        Patient patient
    ) {
        this.patient = patient;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(
        String description
    ) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(
        BigDecimal amount
    ) {
        this.amount = amount;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(
        String rfc
    ) {
        this.rfc = rfc;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(
        String businessName
    ) {
        this.businessName =
            businessName;
    }

    public String getTaxRegime() {
        return taxRegime;
    }

    public void setTaxRegime(
        String taxRegime
    ) {
        this.taxRegime =
            taxRegime;
    }

    public String getFiscalZipCode() {
        return fiscalZipCode;
    }

    public void setFiscalZipCode(
        String fiscalZipCode
    ) {
        this.fiscalZipCode =
            fiscalZipCode;
    }

    public String getCfdiUse() {
        return cfdiUse;
    }

    public void setCfdiUse(
        String cfdiUse
    ) {
        this.cfdiUse =
            cfdiUse;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(
        String billingEmail
    ) {
        this.billingEmail =
            billingEmail;
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

    public InvoiceRequestStatus getStatus() {
        return status;
    }

    public void setStatus(
        InvoiceRequestStatus status
    ) {
        this.status = status;
    }

    public OffsetDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(
        OffsetDateTime requestedAt
    ) {
        this.requestedAt =
            requestedAt;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(
        OffsetDateTime processedAt
    ) {
        this.processedAt =
            processedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(
        String notes
    ) {
        this.notes = notes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}