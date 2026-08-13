package com.cadi.artedental.agenda.controller;
import com.cadi.artedental.agenda.dto.request.*;
import com.cadi.artedental.agenda.dto.response.AgendaAppointmentResponse;
import com.cadi.artedental.agenda.service.AgendaAppointmentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agenda/appointments")
public class AgendaAppointmentController {

    private final AgendaAppointmentService service;

    public AgendaAppointmentController(
        AgendaAppointmentService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<AgendaAppointmentResponse> findAll(
        @RequestParam(required = false)
        String contactId,

        @RequestParam(required = false)
        @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
        )
        OffsetDateTime start,

        @RequestParam(required = false)
        @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
        )
        OffsetDateTime end
    ) {
        if (contactId != null
            && !contactId.isBlank()) {
            return service.findByContact(contactId);
        }

        if (start != null && end != null) {
            return service.findBetween(start, end);
        }

        return service.findAll();
    }

    @GetMapping("/{id}")
    public AgendaAppointmentResponse findById(
        @PathVariable String id
    ) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendaAppointmentResponse create(
        @Valid @RequestBody
        CreateAgendaAppointmentRequest request
    ) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public AgendaAppointmentResponse update(
        @PathVariable String id,
        @Valid @RequestBody
        UpdateAgendaAppointmentRequest request
    ) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public AgendaAppointmentResponse updateStatus(
        @PathVariable String id,
        @Valid @RequestBody
        UpdateAgendaAppointmentStatusRequest request
    ) {
        return service.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
