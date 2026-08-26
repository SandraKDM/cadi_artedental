package com.cadi.artedental.treatment.payment.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.treatment.model.Treatment;

@Entity
@Table(name = "treatment_payments")
public class TreatmentPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
        name = "id",
        nullable = false,
        updatable = false
    )
    private UUID id;

    // =========================================================
    // TREATMENT
    // =========================================================

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(
        name = "treatment_id",
        nullable = false,
        foreignKey = @ForeignKey(
            name = "fk_treatment_payments_treatment"
        )
    )
    private Treatment treatment;

    // =========================================================
    // PAYMENT
    // =========================================================

    @Column(
        name = "amount",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal amount;

    @Column(
        name = "payment_date",
        nullable = false
    )
    private OffsetDateTime paymentDate;

    @Column(
        name = "payment_method",
        length = 50
    )
    private String paymentMethod;

    @Column(
        name = "reference",
        length = 150
    )
    private String reference;

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
        nullable = false,
        updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(
        name = "updated_at",
        nullable = false
    )
    private OffsetDateTime updatedAt;

    // =========================================================
    // LIFECYCLE
    // =========================================================

    @PrePersist
    protected void onCreate() {
        final OffsetDateTime now =
            OffsetDateTime.now();

        if (paymentDate == null) {
            paymentDate = now;
        }

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    public TreatmentPayment() {
    }

    public TreatmentPayment(
        Treatment treatment,
        BigDecimal amount,
        OffsetDateTime paymentDate,
        String paymentMethod,
        String reference,
        String notes
    ) {
        this.treatment = treatment;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.reference = reference;
        this.notes = notes;
    }

    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(
        Treatment treatment
    ) {
        this.treatment = treatment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(
        BigDecimal amount
    ) {
        this.amount = amount;
    }

    public OffsetDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(
        OffsetDateTime paymentDate
    ) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
        String paymentMethod
    ) {
        this.paymentMethod = paymentMethod;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(
        String reference
    ) {
        this.reference = reference;
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

    public void setCreatedAt(
        OffsetDateTime createdAt
    ) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
        OffsetDateTime updatedAt
    ) {
        this.updatedAt = updatedAt;
    }
}