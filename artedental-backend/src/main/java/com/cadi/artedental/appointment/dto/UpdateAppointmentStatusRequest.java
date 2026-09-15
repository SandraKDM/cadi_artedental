package com.cadi.artedental.appointment.dto;

import com.cadi.artedental.appointment.model.AppointmentStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateAppointmentStatusRequest(

    @NotNull(
        message = "El estatus de la cita es obligatorio."
    )
    AppointmentStatus status

) {
}