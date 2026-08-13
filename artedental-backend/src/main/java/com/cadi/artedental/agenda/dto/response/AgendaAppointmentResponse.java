package com.cadi.artedental.agenda.dto.response;

import com.cadi.artedental.agenda.model.*;
import java.time.OffsetDateTime;
import com.cadi.artedental.config.TimeConfig;

public record AgendaAppointmentResponse(
        String id,
        String contactId,
        String contactName,
        String contactPhone,
        OffsetDateTime startDateTime,
        int durationMinutes,
        String reason,
        String notes,
        AgendaAppointmentStatus status,
        OffsetDateTime reminderScheduledAt,
        AgendaReminderStatus reminderStatus,
        OffsetDateTime reminderSentAt,
        String reminderError,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public static AgendaAppointmentResponse fromEntity(
            AgendaAppointment appointment) {
        return new AgendaAppointmentResponse(
                appointment.getId(),
                appointment.getContactId(),
                appointment.getContactName(),
                appointment.getContactPhone(),

                appointment
                        .getStartDateTime()
                        .atZoneSameInstant(
                                TimeConfig.APP_ZONE)
                        .toOffsetDateTime(),

                appointment.getDurationMinutes(),
                appointment.getReason(),
                appointment.getNotes(),
                appointment.getStatus(),

                appointment
                        .getReminderScheduledAt()
                        .atZoneSameInstant(
                                TimeConfig.APP_ZONE)
                        .toOffsetDateTime(),

                appointment.getReminderStatus(),

                appointment.getReminderSentAt() == null
                        ? null
                        : appointment
                                .getReminderSentAt()
                                .atZoneSameInstant(
                                        TimeConfig.APP_ZONE)
                                .toOffsetDateTime(),

                appointment.getReminderError(),

                appointment
                        .getCreatedAt()
                        .atZoneSameInstant(
                                TimeConfig.APP_ZONE)
                        .toOffsetDateTime(),

                appointment
                        .getUpdatedAt()
                        .atZoneSameInstant(
                                TimeConfig.APP_ZONE)
                        .toOffsetDateTime());
    }
}
