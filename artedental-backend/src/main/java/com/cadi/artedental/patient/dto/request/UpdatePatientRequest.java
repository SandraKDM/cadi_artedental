package com.cadi.artedental.patient.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdatePatientRequest(

    @NotBlank
    @Size(max = 100)
    String names,

    @NotBlank
    @Size(max = 100)
    String firstName,

    @NotBlank
    @Size(max = 150)
    String lastName,

    @NotBlank
    @Size(max = 30)
    String phone,

    @Email
    @Size(max = 180)
    String email,

    LocalDate birthDate,

    @Size(max = 500)
    String allergies,

    @Size(max = 500)
    String address,

    @Size(max = 1500)
    String notes
) {
}