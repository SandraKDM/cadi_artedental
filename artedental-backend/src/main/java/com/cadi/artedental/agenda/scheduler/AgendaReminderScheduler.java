package com.cadi.artedental.agenda.scheduler;
import com.cadi.artedental.agenda.model.*;
import com.cadi.artedental.agenda.repository.AgendaAppointmentRepository;
import com.cadi.artedental.notification.whatsapp.WhatsAppReminderService;
import org.slf4j.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class AgendaReminderScheduler {

    private static final Logger log =
        LoggerFactory.getLogger(
            AgendaReminderScheduler.class
        );

    private final AgendaAppointmentRepository repository;
    private final WhatsAppReminderService whatsAppService;
    private final Clock clock;

    public AgendaReminderScheduler(
        AgendaAppointmentRepository repository,
        WhatsAppReminderService whatsAppService,
        Clock clock
    ) {
        this.repository = repository;
        this.whatsAppService = whatsAppService;
        this.clock = clock;
    }

    @Scheduled(
        fixedDelayString =
            "${agenda.reminder.scheduler-delay-ms:60000}"
    )
    @Transactional
    public void processDueReminders() {
        OffsetDateTime now = OffsetDateTime.now(clock);

        List<AgendaAppointment> due =
            repository
                .findByReminderStatusAndReminderScheduledAtLessThanEqualOrderByReminderScheduledAtAsc(
                    AgendaReminderStatus.SCHEDULED,
                    now
                );

        for (AgendaAppointment appointment : due) {
            if (!isEligible(appointment, now)) {
                continue;
            }

            try {
                appointment.markReminderProcessing(
                    OffsetDateTime.now(clock)
                );
                repository.save(appointment);

                whatsAppService
                    .sendAppointmentReminder(
                        appointment
                    );

                appointment.markReminderSent(
                    OffsetDateTime.now(clock),
                    OffsetDateTime.now(clock)
                );
            } catch (Exception ex) {
                log.error(
                    "Error enviando recordatorio de cita {}",
                    appointment.getId(),
                    ex
                );

                appointment.markReminderFailed(
                    ex.getMessage(),
                    OffsetDateTime.now(clock)
                );
            }

            repository.save(appointment);
        }
    }

    private boolean isEligible(
        AgendaAppointment appointment,
        OffsetDateTime now
    ) {
        if (appointment
            .getStartDateTime()
            .isBefore(now)) {
            return false;
        }

        return appointment.getStatus()
                == AgendaAppointmentStatus.SCHEDULED
            || appointment.getStatus()
                == AgendaAppointmentStatus.CONFIRMED;
    }
}
