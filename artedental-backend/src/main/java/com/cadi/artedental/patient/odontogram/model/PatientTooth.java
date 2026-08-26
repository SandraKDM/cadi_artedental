package com.cadi.artedental.patient.odontogram.model;

import com.cadi.artedental.patient.model.Patient;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "patient_teeth", uniqueConstraints = {
        @UniqueConstraint(name = "uk_patient_tooth", columnNames = {
                "patient_id",
                "tooth_number"
        })
}, indexes = {
        @Index(name = "idx_patient_teeth_patient", columnList = "patient_id")
})
public class PatientTooth {

    @Id
    @Column(nullable = false, length = 64)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ToothStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "tooth_number", nullable = false, length = 10)
    private String toothNumber;

    @Column(name = "dentition_type", nullable = false, length = 20)
    private String dentitionType;

    @Column(name = "tooth_type", nullable = false, length = 20)
    private String toothType;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    protected PatientTooth() {
    }

    public PatientTooth(
            String id,
            Patient patient,
            String toothNumber,
            String dentitionType,
            String toothType,
            ToothStatus status,
            String diagnosis,
            String notes,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.patient = patient;
        this.toothNumber = toothNumber;
        this.dentitionType = dentitionType;
        this.toothType = toothType;
        this.status = status;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getToothNumber() {
        return toothNumber;
    }

    public ToothStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getDentitionType() {
        return dentitionType;
    }

    public String getToothType() {
        return toothType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void update(
            String dentitionType,
            String toothType,
            ToothStatus status,
            String diagnosis,
            String notes,
            OffsetDateTime updatedAt) {
        this.dentitionType = dentitionType;
        this.toothType = toothType;
        this.status = status;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.updatedAt = updatedAt;
    }
}