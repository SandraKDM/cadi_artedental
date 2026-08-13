package com.cadi.artedental.agenda.controller;

import com.cadi.artedental.agenda.dto.request.*;
import com.cadi.artedental.agenda.dto.response.AgendaContactResponse;
import com.cadi.artedental.agenda.service.AgendaContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda/contacts")
public class AgendaContactController {

    private final AgendaContactService service;

    public AgendaContactController(
        AgendaContactService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<AgendaContactResponse> findAll(
        @RequestParam(required = false) String q
    ) {
        return service.search(q);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgendaContactResponse create(
        @Valid @RequestBody
        CreateAgendaContactRequest request
    ) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public AgendaContactResponse update(
        @PathVariable String id,
        @Valid @RequestBody
        UpdateAgendaContactRequest request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
