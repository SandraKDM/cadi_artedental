package com.cadi.artedental.notification.whatsapp;
import com.cadi.artedental.agenda.model.AgendaAppointment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class LoggingWhatsAppReminderService
    implements WhatsAppReminderService {

    private static final Logger log =
        LoggerFactory.getLogger(
            LoggingWhatsAppReminderService.class
        );

    private final ZoneId applicationZone;

    public LoggingWhatsAppReminderService(
        Clock clock
    ) {
        this.applicationZone =
            clock.getZone();
    }

    @Override
    public void sendAppointmentReminder(
        AgendaAppointment appointment
    ) {

        ZonedDateTime localAppointmentDate =
            appointment
                .getStartDateTime()
                .atZoneSameInstant(
                    applicationZone
                );

        log.info(
            "SIMULACIÓN WhatsApp -> {} ({}) | "
                + "Cita: {} | Motivo: {}",
            appointment.getContactName(),
            appointment.getContactPhone(),
            localAppointmentDate,
            appointment.getReason()
        );
    }
}