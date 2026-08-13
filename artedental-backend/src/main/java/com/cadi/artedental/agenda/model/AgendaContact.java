package com.cadi.artedental.agenda.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "agenda_contacts")
public class AgendaContact {

    @Id
    @Column(nullable = false, length = 64)
    private String id;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, unique = true, length = 30)
    private String phone;

    @Column(length = 180)
    private String email;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    protected AgendaContact() {}

    public AgendaContact(
        String id,
        String fullName,
        String phone,
        String email,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void update(
        String fullName,
        String phone,
        String email,
        OffsetDateTime updatedAt
    ) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.updatedAt = updatedAt;
    }
}
