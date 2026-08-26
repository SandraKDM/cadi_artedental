package com.cadi.artedental.treatment.model;

import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.patient.odontogram.model.PatientTooth;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "patient_treatments", indexes = {
        @Index(name = "idx_patient_treatments_patient_id", columnList = "patient_id"),
        @Index(name = "idx_patient_treatments_patient_tooth_id", columnList = "patient_tooth_id"),
        @Index(name = "idx_patient_treatments_status", columnList = "status")
})
public class Treatment {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    // =========================================================
    // RELACIONES
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_tooth_id")
    private PatientTooth patientTooth;

    // =========================================================
    // TRATAMIENTO
    // =========================================================

    @Column(name = "treatment_name", nullable = false, length = 200)
    private String treatmentName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "amount_paid", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "progress", nullable = false, precision = 5, scale = 4)
    private BigDecimal progress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TreatmentStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // =========================================================
    // FECHAS
    // =========================================================

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "next_appointment")
    private OffsetDateTime nextAppointment;

    @Column(name = "completed_date")
    private OffsetDateTime completedDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // =========================================================
    // CONSTRUCTOR JPA
    // =========================================================

    protected Treatment() {
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Treatment(
            String id,
            Patient patient,
            PatientTooth patientTooth,
            String treatmentName,
            String description,
            BigDecimal cost,
            BigDecimal amountPaid,
            BigDecimal progress,
            TreatmentStatus status,
            String notes,
            OffsetDateTime startDate,
            OffsetDateTime nextAppointment,
            OffsetDateTime completedDate) {
        this.id = id;
        this.patient = patient;
        this.patientTooth = patientTooth;
        this.treatmentName = treatmentName;
        this.description = description;
        this.cost = cost;
        this.amountPaid = amountPaid;
        this.progress = progress;
        this.status = status;
        this.notes = notes;
        this.startDate = startDate;
        this.nextAppointment = nextAppointment;
        this.completedDate = completedDate;
    }

    // =========================================================
    // TIMESTAMPS
    // =========================================================

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (startDate == null) {
            startDate = now;
        }

        if (cost == null) {
            cost = BigDecimal.ZERO;
        }

        if (amountPaid == null) {
            amountPaid = BigDecimal.ZERO;
        }

        if (progress == null) {
            progress = BigDecimal.ZERO;
        }

        if (status == null) {
            status = TreatmentStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public void update(
            PatientTooth patientTooth,
            String treatmentName,
            String description,
            BigDecimal cost,
            BigDecimal amountPaid,
            BigDecimal progress,
            TreatmentStatus status,
            String notes,
            OffsetDateTime startDate,
            OffsetDateTime nextAppointment,
            OffsetDateTime completedDate) {
        this.patientTooth = patientTooth;
        this.treatmentName = treatmentName;
        this.description = description;
        this.cost = cost;
        this.amountPaid = amountPaid;
        this.progress = progress;
        this.status = status;
        this.notes = notes;
        this.startDate = startDate;
        this.nextAppointment = nextAppointment;
        this.completedDate = completedDate;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public PatientTooth getPatientTooth() {
        return patientTooth;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public BigDecimal getProgress() {
        return progress;
    }

    public TreatmentStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public OffsetDateTime getNextAppointment() {
        return nextAppointment;
    }

    public OffsetDateTime getCompletedDate() {
        return completedDate;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}