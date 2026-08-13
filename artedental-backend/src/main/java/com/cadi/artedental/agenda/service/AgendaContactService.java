package com.cadi.artedental.agenda.service;

import com.cadi.artedental.agenda.dto.request.*;
import com.cadi.artedental.agenda.dto.response.AgendaContactResponse;
import com.cadi.artedental.agenda.model.AgendaContact;
import com.cadi.artedental.agenda.repository.AgendaContactRepository;
import com.cadi.artedental.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class AgendaContactService {

    private final AgendaContactRepository repository;
    private final Clock clock;

    public AgendaContactService(
        AgendaContactRepository repository,
        Clock clock
    ) {
        this.repository = repository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<AgendaContactResponse> findAll() {
        return repository.findAll()
            .stream()
            .map(AgendaContactResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<AgendaContactResponse> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }

        String q = query.trim();

        return repository
            .findByFullNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCase(
                q,
                q.replaceAll("\\D", ""),
                q
            )
            .stream()
            .map(AgendaContactResponse::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public AgendaContact getEntity(String id) {
        return repository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    "No se encontró el contacto " + id + "."
                )
            );
    }

    @Transactional
    public AgendaContactResponse create(
        CreateAgendaContactRequest request
    ) {
        String phone = normalizePhone(request.phone());

        if (repository.existsByPhone(phone)) {
            throw new BusinessRuleException(
                "Ya existe un contacto con ese teléfono."
            );
        }

        OffsetDateTime now = OffsetDateTime.now(clock);

        AgendaContact contact = new AgendaContact(
            UUID.randomUUID().toString(),
            request.fullName().trim(),
            phone,
            normalizeEmail(request.email()),
            now,
            now
        );

        return AgendaContactResponse.fromEntity(
            repository.save(contact)
        );
    }

    @Transactional
    public AgendaContactResponse update(
        String id,
        UpdateAgendaContactRequest request
    ) {
        AgendaContact contact = getEntity(id);

        contact.update(
            request.fullName().trim(),
            normalizePhone(request.phone()),
            normalizeEmail(request.email()),
            OffsetDateTime.now(clock)
        );

        return AgendaContactResponse.fromEntity(
            repository.save(contact)
        );
    }

    @Transactional
    public void delete(String id) {
        repository.delete(getEntity(id));
    }

    private String normalizePhone(String phone) {
        String normalized = phone.replaceAll("\\D", "");

        if (normalized.length() < 10) {
            throw new BusinessRuleException(
                "El teléfono debe contener al menos 10 dígitos."
            );
        }

        return normalized;
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.trim().toLowerCase();
    }
}
