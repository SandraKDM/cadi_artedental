package com.cadi.artedental.treatment.repository;

import com.cadi.artedental.treatment.model.Treatment;
import com.cadi.artedental.treatment.model.TreatmentPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanTreatmentRepository
    extends JpaRepository<TreatmentPlan, String> {

    List<TreatmentPlan> findByPatient_IdOrderByCreatedAtDesc(
        String patientId
    );

    Optional<TreatmentPlan> findByIdAndPatient_Id(
        String id,
        String patientId
    );

    @Query("""
        SELECT t
        FROM TreatmentPlan t
        JOIN t.patientTooth pt
        WHERE t.patient.id = :patientId
          AND pt.toothNumber = :toothNumber
        ORDER BY t.createdAt DESC
    """)
    List<TreatmentPlan> findByPatientIdAndToothNumber(
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

    List<TreatmentPlan> findByPatient_Id(
    String patientId
);
}