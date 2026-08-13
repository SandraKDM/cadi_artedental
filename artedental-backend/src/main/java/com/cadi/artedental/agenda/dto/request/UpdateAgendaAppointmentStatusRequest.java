package com.cadi.artedental.agenda.dto.request;

import com.cadi.artedental.agenda.model.AgendaAppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateAgendaAppointmentStatusRequest(
    @NotNull AgendaAppointmentStatus status
) {}
