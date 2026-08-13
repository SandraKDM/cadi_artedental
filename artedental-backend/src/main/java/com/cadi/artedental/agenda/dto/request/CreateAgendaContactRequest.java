package com.cadi.artedental.agenda.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAgendaContactRequest(
    @NotBlank @Size(max = 150) String fullName,
    @NotBlank @Size(max = 30) String phone,
    @Email @Size(max = 180) String email
) {}
