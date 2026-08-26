package com.cadi.artedental.patient.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "patients", indexes = {
        @Index(name = "idx_patient_name", columnList = "first_name,last_name"),
        @Index(name = "idx_patient_phone", columnList = "phone"),
        @Index(name = "idx_patient_email", columnList = "email")
})
public class Patient {

    @Id
    @Column(nullable = false, length = 64)
    private String id;

    @Column(name = "names", nullable = false, length = 100)
    private String names;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(length = 180)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(length = 500)
    private String allergies;

    @Column(length = 500)
    private String address;

    @Column(length = 1500)
    private String notes;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Patient() {
    }

    public Patient(
            String id,
            String names,
            String firstName,
            String lastName,
            String phone,
            String email,
            LocalDate birthDate,
            String allergies,
            String address,
            String notes,
            boolean active,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.names = names;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.allergies = allergies;
        this.address = address;
        this.notes = notes;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getAddress() {
        return address;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public void update(
            String names,
            String firstName,
            String lastName,
            String phone,
            String email,
            LocalDate birthDate,
            String allergies,
            String address,
            String notes,
            OffsetDateTime updatedAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.birthDate = birthDate;
        this.allergies = allergies;
        this.address = address;
        this.notes = notes;
        this.updatedAt = updatedAt;
    }

    public void deactivate(
            OffsetDateTime updatedAt) {
        this.active = false;
        this.updatedAt = updatedAt;
    }

    public void activate(
            OffsetDateTime updatedAt) {
        this.active = true;
        this.updatedAt = updatedAt;
    }
}