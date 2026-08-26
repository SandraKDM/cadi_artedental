package com.cadi.artedental.treatment.repository;

import com.cadi.artedental.treatment.model.Treatment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TreatmentRepository
    extends JpaRepository<Treatment, String> {

    List<Treatment> findByPatient_IdOrderByStartDateDesc(
        String patientId
    );

    Optional<Treatment> findByIdAndPatient_Id(
        String id,
        String patientId
    );

    @Query("""
        SELECT t
        FROM Treatment t
        JOIN t.patientTooth pt
        WHERE t.patient.id = :patientId
          AND pt.toothNumber = :toothNumber
        ORDER BY t.startDate DESC
    """)
    List<Treatment> findByPatientIdAndToothNumber(
        @Param("patientId")
        String patientId,

        @Param("toothNumber")
        String toothNumber
    );

    boolean existsByIdAndPatient_Id(
        String id,
        String patientId
    );

    void deleteByPatient_Id(
        String patientId
    );

    List<Treatment> findByPatient_Id(
    String patientId
);
}