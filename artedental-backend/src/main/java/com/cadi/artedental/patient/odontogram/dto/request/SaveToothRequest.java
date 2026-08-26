package com.cadi.artedental.patient.odontogram.dto.request;

import com.cadi.artedental.patient.odontogram.model.ToothStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveToothRequest(

    @NotBlank
    String dentitionType,

    @NotBlank
    String toothType,

    @NotNull
    ToothStatus status,

    String diagnosis,

    String notes

) {}