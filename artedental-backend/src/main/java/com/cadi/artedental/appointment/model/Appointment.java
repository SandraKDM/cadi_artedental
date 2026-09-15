package com.cadi.artedental.appointment.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.cadi.artedental.patient.model.Patient;

import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    // =========================================================
    // PATIENT
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // =========================================================
    // DATE / TIME
    // =========================================================

    @Column(name = "start_date_time", nullable = false)
    private OffsetDateTime startDateTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    // =========================================================
    // APPOINTMENT DATA
    // =========================================================

    @Column(name = "reason", length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private AppointmentStatus status;

    // =========================================================
    // COST
    // =========================================================

    @Column(name = "cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal cost = BigDecimal.ZERO;

    // =========================================================
    // NOTES
    // =========================================================

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // =========================================================
    // AUDIT
    // =========================================================

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Appointment() {
    }

    // =========================================================
    // CALLBACKS
    // =========================================================

    @PrePersist
    protected void onCreate() {

        if (id == null) {
            id = UUID.randomUUID()
                    .toString();
        }

        OffsetDateTime now = OffsetDateTime.now();

        if (status == null) {
            status = AppointmentStatus.SCHEDULED;
        }

        if (cost == null) {
            cost = BigDecimal.ZERO;
        }

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

    public String getId() {
        return id;
    }

    public void setId(
            String id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(
            Patient patient) {
        this.patient = patient;
    }

    public OffsetDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(
            OffsetDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(
            Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(
            String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(
            AppointmentStatus status) {
        this.status = status;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(
            BigDecimal cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(
            String notes) {
        this.notes = notes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}