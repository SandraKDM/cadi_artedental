package com.cadi.artedental.patient.clinicalhistory.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cadi.artedental.patient.clinicalhistory.model.MedicationData;

public record SaveClinicalHistoryRequest(

    String gender,

    String bloodType,

    String maritalStatus,

    String occupation,

    String emergencyContactName,

    String emergencyContactPhone,

    Set<String> consultationReasons,

    String otherConsultationReason,

    Map<String, Boolean> familyHistory,

    String familyHistoryNotes,

    Map<String, Boolean> pathologicalHistory,

    String medicationAllergies,

    String foodLatexAllergies,

    List<MedicationData> medications,

    String vaccinationStatus,

    String lastVaccine,

    Boolean bisphosphonates,

    Boolean previousDentalAnesthesia,

    String anesthesiaReaction,

    String lastDentalTreatmentDate,

    String lastDentalTreatment,

    String smoking,

    String alcohol,

    String drugs,

    String exercise,

    String sleepHours,

    String diet,

    String pregnancy,

    String breastfeeding,

    Map<String, Set<String>> systems,

    String systemsNotes,

    Set<String> hygieneHabits,

    Set<String> parafunctions,

    Set<String> extraoralFindings,

    Set<String> intraoralFindings,

    String explorationNotes,

    String complementaryStudies,

    String mainDiagnosis,

    String secondaryDiagnosis,

    String prognosis,

    String phase1,

    String phase2,

    String phase3,

    @NotNull
    Boolean consentAccepted

) {}