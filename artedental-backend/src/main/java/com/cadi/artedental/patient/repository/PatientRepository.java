package com.cadi.artedental.patient.repository;

import com.cadi.artedental.patient.model.Patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository
    extends JpaRepository<Patient, String> {

    List<Patient> findByActiveTrueOrderByFirstNameAsc();

    boolean existsByPhone(
        String phone
    );

    List<Patient>
        findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhoneContainingOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String phone,
            String email
        );
}