package com.cadi.artedental.agenda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(
    name = "agenda_appointments",
    indexes = {
        @Index(name = "idx_agenda_appointment_contact", columnList = "contact_id"),
        @Index(name = "idx_agenda_appointment_start", columnList = "start_date_time"),
        @Index(name = "idx_agenda_reminder_due", columnList = "reminder_status, reminder_scheduled_at")
    }
)
public class AgendaAppointment {

    @Id
    @Column(nullable = false, length = 64)
    private String id;

    @Column(name = "contact_id", nullable = false, length = 64)
    private String contactId;

    @Column(name = "contact_name", nullable = false, length = 150)
    private String contactName;

    @Column(name = "contact_phone", nullable = false, length = 30)
    private String contactPhone;

    @Column(name = "start_date_time", nullable = false)
    private OffsetDateTime startDateTime;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(nullable = false, length = 250)
    private String reason;

    @Column(length = 1500)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AgendaAppointmentStatus status;

    @Column(name = "reminder_scheduled_at", nullable = false)
    private OffsetDateTime reminderScheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "reminder_status", nullable = false, length = 30)
    private AgendaReminderStatus reminderStatus;

    @Column(name = "reminder_sent_at")
    private OffsetDateTime reminderSentAt;

    @Column(name = "reminder_error", length = 1500)
    private String reminderError;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    protected AgendaAppointment() {}

    public AgendaAppointment(
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
        OffsetDateTime updatedAt
    ) {
        this.id = id;
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.startDateTime = startDateTime;
        this.durationMinutes = durationMinutes;
        this.reason = reason;
        this.notes = notes;
        this.status = status;
        this.reminderScheduledAt = reminderScheduledAt;
        this.reminderStatus = reminderStatus;
        this.reminderSentAt = reminderSentAt;
        this.reminderError = reminderError;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public String getContactId() { return contactId; }
    public String getContactName() { return contactName; }
    public String getContactPhone() { return contactPhone; }
    public OffsetDateTime getStartDateTime() { return startDateTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getReason() { return reason; }
    public String getNotes() { return notes; }
    public AgendaAppointmentStatus getStatus() { return status; }
    public OffsetDateTime getReminderScheduledAt() { return reminderScheduledAt; }
    public AgendaReminderStatus getReminderStatus() { return reminderStatus; }
    public OffsetDateTime getReminderSentAt() { return reminderSentAt; }
    public String getReminderError() { return reminderError; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public OffsetDateTime getEndDateTime() {
        return startDateTime.plusMinutes(durationMinutes);
    }

    public void updateDetails(
        String contactId,
        String contactName,
        String contactPhone,
        OffsetDateTime startDateTime,
        int durationMinutes,
        String reason,
        String notes,
        OffsetDateTime updatedAt
    ) {
        boolean startChanged = !this.startDateTime.equals(startDateTime);

        this.contactId = contactId;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.startDateTime = startDateTime;
        this.durationMinutes = durationMinutes;
        this.reason = reason;
        this.notes = notes;
        this.updatedAt = updatedAt;

        if (startChanged && reminderStatus != AgendaReminderStatus.SENT) {
            this.reminderScheduledAt = startDateTime.minusHours(24);
            this.reminderStatus = AgendaReminderStatus.SCHEDULED;
            this.reminderSentAt = null;
            this.reminderError = null;
        }
    }

    public void changeStatus(
        AgendaAppointmentStatus newStatus,
        OffsetDateTime updatedAt
    ) {
        this.status = newStatus;
        this.updatedAt = updatedAt;

        if ((newStatus == AgendaAppointmentStatus.CANCELLED
            || newStatus == AgendaAppointmentStatus.COMPLETED
            || newStatus == AgendaAppointmentStatus.NO_SHOW)
            && reminderStatus != AgendaReminderStatus.SENT) {
            reminderStatus = AgendaReminderStatus.CANCELLED;
            reminderError = null;
        }
    }

    public void markReminderProcessing(OffsetDateTime updatedAt) {
        reminderStatus = AgendaReminderStatus.PROCESSING;
        reminderError = null;
        this.updatedAt = updatedAt;
    }

    public void markReminderSent(
        OffsetDateTime sentAt,
        OffsetDateTime updatedAt
    ) {
        reminderStatus = AgendaReminderStatus.SENT;
        reminderSentAt = sentAt;
        reminderError = null;
        this.updatedAt = updatedAt;
    }

    public void markReminderFailed(
        String error,
        OffsetDateTime updatedAt
    ) {
        reminderStatus = AgendaReminderStatus.FAILED;
        reminderError = error;
        this.updatedAt = updatedAt;
    }
}
