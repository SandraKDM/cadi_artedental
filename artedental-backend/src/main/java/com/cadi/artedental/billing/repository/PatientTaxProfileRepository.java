package com.cadi.artedental.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadi.artedental.billing.model.PatientTaxProfile;

@Repository
public interface PatientTaxProfileRepository
    extends JpaRepository<PatientTaxProfile, String> {

    Optional<PatientTaxProfile> findByPatient_Id(
        String patientId
    );

    boolean existsByPatient_Id(
        String patientId
    );
}