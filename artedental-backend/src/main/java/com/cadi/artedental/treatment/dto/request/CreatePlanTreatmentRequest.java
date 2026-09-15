package com.cadi.artedental.treatment.dto.request;

import com.cadi.artedental.treatment.model.TreatmentStatus;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreatePlanTreatmentRequest(

    @NotBlank(message = "treatmentName no debe estar vacío")
    @Size(
        max = 200,
        message = "treatmentName no puede superar 200 caracteres"
    )
    String treatmentName,

    String description,

    // Opcional.
    // Ejemplo: "36".
    // null = tratamiento general.
    String toothNumber,

    @NotNull(message = "cost es obligatorio")
    @PositiveOrZero(message = "cost no puede ser negativo")
    BigDecimal cost,

    @PositiveOrZero(message = "amountPaid no puede ser negativo")
    BigDecimal amountPaid,

    @DecimalMin(
        value = "0.0",
        message = "progress no puede ser menor que 0"
    )
    @DecimalMax(
        value = "1.0",
        message = "progress no puede ser mayor que 1"
    )
    BigDecimal progress,

    TreatmentStatus status,

    String notes,

    OffsetDateTime startDate,

    OffsetDateTime nextAppointment

) {
}