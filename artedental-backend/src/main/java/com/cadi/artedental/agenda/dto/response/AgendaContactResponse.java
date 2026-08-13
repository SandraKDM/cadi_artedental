package com.cadi.artedental.agenda.dto.response;

import com.cadi.artedental.agenda.model.AgendaContact;
import java.time.OffsetDateTime;

public record AgendaContactResponse(
    String id,
    String fullName,
    String phone,
    String email,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static AgendaContactResponse fromEntity(
        AgendaContact contact
    ) {
        return new AgendaContactResponse(
            contact.getId(),
            contact.getFullName(),
            contact.getPhone(),
            contact.getEmail(),
            contact.getCreatedAt(),
            contact.getUpdatedAt()
        );
    }
}
