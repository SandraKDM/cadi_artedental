package com.cadi.artedental.patient.dto.response;

import com.cadi.artedental.patient.model.Patient;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record PatientResponse(

        String id,
        String names,
        String firstName,
        String lastName,
        String fullName,
        String phone,
        String email,
        LocalDate birthDate,
        String allergies,
        String address,
        String notes,
        boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public static PatientResponse fromEntity(
            Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getNames(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getFullName(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getBirthDate(),
                patient.getAllergies(),
                patient.getAddress(),
                patient.getNotes(),
                patient.isActive(),
                patient.getCreatedAt(),
                patient.getUpdatedAt());
    }
}