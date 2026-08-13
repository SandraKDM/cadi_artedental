package com.cadi.artedental.agenda.service;

import com.cadi.artedental.agenda.dto.request.*;
import com.cadi.artedental.agenda.dto.response.AgendaAppointmentResponse;
import com.cadi.artedental.agenda.model.*;
import com.cadi.artedental.agenda.repository.AgendaAppointmentRepository;
import com.cadi.artedental.exception.BusinessRuleException;
import com.cadi.artedental.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class AgendaAppointmentService {

    private final AgendaAppointmentRepository repository;
    private final AgendaContactService contactService;
    private final Clock clock;

    public AgendaAppointmentService(
        AgendaAppointmentRepository repository,
        AgendaContactService contactService,
        Clock clock
    ) {
        this.repository = repository;
        this.contactService = contactService;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<AgendaAppointmentResponse> findAll() {
        return repository.findAllByOrderByStartDateTimeAsc()
            .stream()
            .map(AgendaAppointmentResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public AgendaAppointmentResponse findById(String id) {
        return AgendaAppointmentResponse.fromEntity(
            getEntity(id)
        );
    }

    @Transactional(readOnly = true)
    public List<AgendaAppointmentResponse> findByContact(
        String contactId
    ) {
        return repository
            .findByContactIdOrderByStartDateTimeDesc(contactId)
            .stream()
            .map(AgendaAppointmentResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<AgendaAppointmentResponse> findBetween(
        OffsetDateTime start,
        OffsetDateTime end
    ) {
        if (end.isBefore(start)) {
            throw new BusinessRuleException(
                "La fecha final no puede ser anterior a la inicial."
            );
        }

        return repository
            .findByStartDateTimeGreaterThanEqualAndStartDateTimeLessThanOrderByStartDateTimeAsc(
                start, end
            )
            .stream()
            .map(AgendaAppointmentResponse::fromEntity)
            .toList();
    }

    @Transactional
    public AgendaAppointmentResponse create(
        CreateAgendaAppointmentRequest request
    ) {
        var contact = contactService.getEntity(
            request.contactId()
        );

        validateNoOverlap(
            null,
            request.startDateTime(),
            request.durationMinutes()
        );

        OffsetDateTime now = OffsetDateTime.now(clock);

        AgendaAppointment appointment =
            new AgendaAppointment(
                UUID.randomUUID().toString(),
                contact.getId(),
                contact.getFullName(),
                contact.getPhone(),
                request.startDateTime(),
                request.durationMinutes(),
                request.reason().trim(),
                normalizeNotes(request.notes()),
                AgendaAppointmentStatus.SCHEDULED,
                request.startDateTime().minusHours(24),
                AgendaReminderStatus.SCHEDULED,
                null,
                null,
                now,
                now
            );

        return AgendaAppointmentResponse.fromEntity(
            repository.save(appointment)
        );
    }

    @Transactional
    public AgendaAppointmentResponse update(
        String id,
        UpdateAgendaAppointmentRequest request
    ) {
        AgendaAppointment appointment = getEntity(id);
        var contact = contactService.getEntity(
            request.contactId()
        );

        validateNoOverlap(
            appointment.getId(),
            request.startDateTime(),
            request.durationMinutes()
        );

        appointment.updateDetails(
            contact.getId(),
            contact.getFullName(),
            contact.getPhone(),
            request.startDateTime(),
            request.durationMinutes(),
            request.reason().trim(),
            normalizeNotes(request.notes()),
            OffsetDateTime.now(clock)
        );

        return AgendaAppointmentResponse.fromEntity(
            repository.save(appointment)
        );
    }

    @Transactional
    public AgendaAppointmentResponse updateStatus(
        String id,
        UpdateAgendaAppointmentStatusRequest request
    ) {
        AgendaAppointment appointment = getEntity(id);

        appointment.changeStatus(
            request.status(),
            OffsetDateTime.now(clock)
        );

        return AgendaAppointmentResponse.fromEntity(
            repository.save(appointment)
        );
    }

    @Transactional
    public void delete(String id) {
        repository.delete(getEntity(id));
    }

    @Transactional(readOnly = true)
    public AgendaAppointment getEntity(String id) {
        return repository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    "No se encontró la cita " + id + "."
                )
            );
    }

    private void validateNoOverlap(
        String ignoredId,
        OffsetDateTime start,
        int durationMinutes
    ) {
        OffsetDateTime end =
            start.plusMinutes(durationMinutes);

        List<AgendaAppointment> nearby =
            repository
                .findByStartDateTimeGreaterThanEqualAndStartDateTimeLessThanOrderByStartDateTimeAsc(
                    start.minusHours(8),
                    end.plusHours(8)
                );

        boolean overlaps = nearby.stream()
            .filter(
                a -> ignoredId == null
                    || !a.getId().equals(ignoredId)
            )
            .filter(
                a -> a.getStatus()
                    != AgendaAppointmentStatus.CANCELLED
            )
            .anyMatch(
                a -> start.isBefore(a.getEndDateTime())
                    && end.isAfter(a.getStartDateTime())
            );

        if (overlaps) {
            throw new BusinessRuleException(
                "La cita se cruza con otra cita existente."
            );
        }
    }

    private String normalizeNotes(String notes) {
        if (notes == null || notes.isBlank()) {
            return null;
        }
        return notes.trim();
    }
}
