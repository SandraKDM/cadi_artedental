package com.cadi.artedental.billing.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.cadi.artedental.patient.model.Patient;

import jakarta.persistence.*;

@Entity
@Table(
    name = "patient_tax_profiles",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_patient_tax_profile_patient",
            columnNames = "patient_id"
        )
    }
)
public class PatientTaxProfile {

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

    @OneToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "patient_id",
        nullable = false,
        unique = true
    )
    private Patient patient;

    // =========================================================
    // TAX DATA
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

    public PatientTaxProfile() {
    }

    // =========================================================
    // JPA CALLBACKS
    // =========================================================

    @PrePersist
    protected void onCreate() {

        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        OffsetDateTime now =
            OffsetDateTime.now();

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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}