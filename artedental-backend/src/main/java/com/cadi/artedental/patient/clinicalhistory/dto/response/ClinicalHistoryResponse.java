package com.cadi.artedental.patient.clinicalhistory.dto.response;

import com.cadi.artedental.patient.clinicalhistory.model.ClinicalHistory;
import com.cadi.artedental.patient.clinicalhistory.model.MedicationData;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record ClinicalHistoryResponse(
    String id,
    String patientId,
    LocalDate birthDate,
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
    boolean consentAccepted,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {

    public static ClinicalHistoryResponse fromEntity(
        ClinicalHistory history
    ) {
        return new ClinicalHistoryResponse(
            history.getId(),
            history.getPatient().getId(),
            history.getPatient().getBirthDate(),
            history.getGender(),
            history.getBloodType(),
            history.getMaritalStatus(),
            history.getOccupation(),
            history.getEmergencyContactName(),
            history.getEmergencyContactPhone(),
            history.getConsultationReasons(),
            history.getOtherConsultationReason(),
            history.getFamilyHistory(),
            history.getFamilyHistoryNotes(),
            history.getPathologicalHistory(),
            history.getMedicationAllergies(),
            history.getFoodLatexAllergies(),
            history.getMedications(),
            history.getVaccinationStatus(),
            history.getLastVaccine(),
            history.getBisphosphonates(),
            history.getPreviousDentalAnesthesia(),
            history.getAnesthesiaReaction(),
            history.getLastDentalTreatmentDate(),
            history.getLastDentalTreatment(),
            history.getSmoking(),
            history.getAlcohol(),
            history.getDrugs(),
            history.getExercise(),
            history.getSleepHours(),
            history.getDiet(),
            history.getPregnancy(),
            history.getBreastfeeding(),
            history.getSystems(),
            history.getSystemsNotes(),
            history.getHygieneHabits(),
            history.getParafunctions(),
            history.getExtraoralFindings(),
            history.getIntraoralFindings(),
            history.getExplorationNotes(),
            history.getComplementaryStudies(),
            history.getMainDiagnosis(),
            history.getSecondaryDiagnosis(),
            history.getPrognosis(),
            history.getTreatmentPhase1(),
            history.getTreatmentPhase2(),
            history.getTreatmentPhase3(),
            history.isConsentAccepted(),
            history.getCreatedAt(),
            history.getUpdatedAt()
        );
    }
}
