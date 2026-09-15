package com.cadi.artedental.appointment.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.cadi.artedental.appointment.model.AppointmentStatus;

public record AppointmentResponse(

    String id,

    String patientId,

    String patientName,

    OffsetDateTime startDateTime,

    Integer durationMinutes,

    String reason,

    AppointmentStatus status,

    BigDecimal cost,

    String notes,

    OffsetDateTime createdAt,

    OffsetDateTime updatedAt

) {
}