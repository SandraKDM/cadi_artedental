package com.cadi.artedental.patient.odontogram.repository;

import com.cadi.artedental.patient.odontogram.model.PatientTooth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientToothRepository
    extends JpaRepository<PatientTooth, String> {

    List<PatientTooth>
        findByPatientIdOrderByToothNumberAsc(
            String patientId
        );

    Optional<PatientTooth>
        findByPatientIdAndToothNumber(
            String patientId,
            String toothNumber
        );

    boolean existsByPatientIdAndToothNumber(
        String patientId,
        String toothNumber
    );

    Optional<PatientTooth> findByPatient_IdAndToothNumber(
    String patientId,
    String toothNumber
);
}