package com.cadi.artedental.appointment.repository;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadi.artedental.appointment.model.Appointment;
import com.cadi.artedental.appointment.model.AppointmentStatus;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, String> {

    // =========================================================
    // CITAS POR PACIENTE
    // =========================================================

    List<Appointment>
        findByPatient_IdOrderByStartDateTimeDesc(
            String patientId
        );

    // =========================================================
    // CITAS POR ESTATUS
    // =========================================================

    List<Appointment>
        findByStatusOrderByStartDateTimeAsc(
            AppointmentStatus status
        );

    // =========================================================
    // CITAS EN RANGO DE FECHAS
    // =========================================================

    List<Appointment>
        findByStartDateTimeBetweenOrderByStartDateTimeAsc(
            OffsetDateTime start,
            OffsetDateTime end
        );

    // =========================================================
    // CITAS DE UN PACIENTE EN RANGO
    // =========================================================

    List<Appointment>
        findByPatient_IdAndStartDateTimeBetweenOrderByStartDateTimeAsc(
            String patientId,
            OffsetDateTime start,
            OffsetDateTime end
        );
}