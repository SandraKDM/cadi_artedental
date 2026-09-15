package com.cadi.artedental.appointment.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.cadi.artedental.appointment.model.AppointmentStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateAppointmentRequest(

    @NotNull(
        message = "La fecha y hora de la cita son obligatorias."
    )
    OffsetDateTime startDateTime,

    @NotNull(
        message = "La duración de la cita es obligatoria."
    )
    @Min(
        value = 1,
        message = "La duración debe ser mayor a 0 minutos."
    )
    Integer durationMinutes,

    @Size(
        max = 255,
        message = "El motivo no puede exceder 255 caracteres."
    )
    String reason,

    @NotNull(
        message = "El estatus de la cita es obligatorio."
    )
    AppointmentStatus status,

    @DecimalMin(
        value = "0.00",
        inclusive = true,
        message = "El costo de la cita no puede ser negativo."
    )
    BigDecimal cost,

    String notes

) {
}