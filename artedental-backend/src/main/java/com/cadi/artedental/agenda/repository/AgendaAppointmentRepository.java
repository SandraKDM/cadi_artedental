package com.cadi.artedental.agenda.repository;

import com.cadi.artedental.agenda.model.AgendaAppointment;
import com.cadi.artedental.agenda.model.AgendaReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface AgendaAppointmentRepository
    extends JpaRepository<AgendaAppointment, String> {

    List<AgendaAppointment> findAllByOrderByStartDateTimeAsc();

    List<AgendaAppointment> findByContactIdOrderByStartDateTimeDesc(
        String contactId
    );

    List<AgendaAppointment>
        findByStartDateTimeGreaterThanEqualAndStartDateTimeLessThanOrderByStartDateTimeAsc(
            OffsetDateTime start,
            OffsetDateTime end
        );

    List<AgendaAppointment>
        findByReminderStatusAndReminderScheduledAtLessThanEqualOrderByReminderScheduledAtAsc(
            AgendaReminderStatus status,
            OffsetDateTime dueAt
        );
}
