package com.cadi.artedental.agenda.dto.request;

import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;

public record CreateAgendaAppointmentRequest(
    @NotBlank String contactId,
    @NotNull OffsetDateTime startDateTime,
    @Min(1) @Max(480) int durationMinutes,
    @NotBlank @Size(max = 250) String reason,
    @Size(max = 1500) String notes
) {}
