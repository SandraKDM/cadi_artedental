package com.cadi.artedental.patient.clinicalhistory.repository;

import com.cadi.artedental.patient.clinicalhistory.model.ClinicalHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicalHistoryRepository
    extends JpaRepository<ClinicalHistory, String> {

    Optional<ClinicalHistory>
        findByPatientId(
            String patientId
        );

    boolean existsByPatientId(
        String patientId
    );
}