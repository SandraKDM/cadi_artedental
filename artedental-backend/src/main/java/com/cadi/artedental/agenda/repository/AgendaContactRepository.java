package com.cadi.artedental.agenda.repository;

import com.cadi.artedental.agenda.model.AgendaContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaContactRepository
    extends JpaRepository<AgendaContact, String> {

    boolean existsByPhone(String phone);

    List<AgendaContact>
        findByFullNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCase(
            String fullName,
            String phone,
            String email
        );
}
