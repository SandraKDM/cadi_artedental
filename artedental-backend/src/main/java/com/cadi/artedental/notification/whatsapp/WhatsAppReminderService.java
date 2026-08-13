package com.cadi.artedental.notification.whatsapp;

import com.cadi.artedental.agenda.model.AgendaAppointment;

public interface WhatsAppReminderService {

    /**
     * Envía el recordatorio de una cita por WhatsApp.
     *
     * La implementación concreta puede usar:
     * - Logging para pruebas.
     * - WhatsApp Business Cloud API en producción.
     *
     * Si ocurre un error durante el envío,
     * debe lanzar una excepción para que el scheduler
     * pueda marcar el recordatorio como FAILED.
     */
    void sendAppointmentReminder(
        AgendaAppointment appointment
    );
}