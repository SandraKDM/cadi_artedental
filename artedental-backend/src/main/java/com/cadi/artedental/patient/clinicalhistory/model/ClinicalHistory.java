package com.cadi.artedental.patient.clinicalhistory.model;

import com.cadi.artedental.patient.model.Patient;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "patient_clinical_histories", uniqueConstraints = {
        @UniqueConstraint(name = "uk_clinical_history_patient", columnNames = "patient_id")
})
public class ClinicalHistory {

    @Id
    @Column(nullable = false, length = 64)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    @Column(length = 50)
    private String gender;

    @Column(name = "blood_type", length = 10)
    private String bloodType;

    @Column(name = "marital_status", length = 50)
    private String maritalStatus;

    @Column(length = 150)
    private String occupation;

    @Column(name = "emergency_contact_name", length = 150)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 30)
    private String emergencyContactPhone;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "consultation_reasons", columnDefinition = "jsonb")
    private Set<String> consultationReasons;

    @Column(name = "other_consultation_reason", columnDefinition = "TEXT")
    private String otherConsultationReason;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "family_history", columnDefinition = "jsonb")
    private Map<String, Boolean> familyHistory;

    @Column(name = "family_history_notes", columnDefinition = "TEXT")
    private String familyHistoryNotes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "pathological_history", columnDefinition = "jsonb")
    private Map<String, Boolean> pathologicalHistory;

    @Column(name = "medication_allergies", columnDefinition = "TEXT")
    private String medicationAllergies;

    @Column(name = "food_latex_allergies", columnDefinition = "TEXT")
    private String foodLatexAllergies;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "medications", columnDefinition = "jsonb")
    private List<MedicationData> medications;

    @Column(name = "vaccination_status", columnDefinition = "TEXT")
    private String vaccinationStatus;

    @Column(name = "last_vaccine", columnDefinition = "TEXT")
    private String lastVaccine;

    @Column(name = "bisphosphonates")
    private Boolean bisphosphonates;

    @Column(name = "previous_dental_anesthesia")
    private Boolean previousDentalAnesthesia;

    @Column(name = "anesthesia_reaction", columnDefinition = "TEXT")
    private String anesthesiaReaction;

    @Column(name = "last_dental_treatment_date", length = 50)
    private String lastDentalTreatmentDate;

    @Column(name = "last_dental_treatment", columnDefinition = "TEXT")
    private String lastDentalTreatment;

    @Column(columnDefinition = "TEXT")
    private String smoking;

    @Column(columnDefinition = "TEXT")
    private String alcohol;

    @Column(columnDefinition = "TEXT")
    private String drugs;

    @Column(columnDefinition = "TEXT")
    private String exercise;

    @Column(name = "sleep_hours", columnDefinition = "TEXT")
    private String sleepHours;

    @Column(columnDefinition = "TEXT")
    private String diet;

    @Column(columnDefinition = "TEXT")
    private String pregnancy;

    @Column(columnDefinition = "TEXT")
    private String breastfeeding;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "systems", columnDefinition = "jsonb")
    private Map<String, Set<String>> systems;

    @Column(name = "systems_notes", columnDefinition = "TEXT")
    private String systemsNotes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "hygiene_habits", columnDefinition = "jsonb")
    private Set<String> hygieneHabits;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parafunctions", columnDefinition = "jsonb")
    private Set<String> parafunctions;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extraoral_findings", columnDefinition = "jsonb")
    private Set<String> extraoralFindings;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "intraoral_findings", columnDefinition = "jsonb")
    private Set<String> intraoralFindings;

    @Column(name = "exploration_notes", columnDefinition = "TEXT")
    private String explorationNotes;

    @Column(name = "complementary_studies", columnDefinition = "TEXT")
    private String complementaryStudies;

    @Column(name = "main_diagnosis", columnDefinition = "TEXT")
    private String mainDiagnosis;

    @Column(name = "secondary_diagnosis", columnDefinition = "TEXT")
    private String secondaryDiagnosis;

    @Column(length = 50)
    private String prognosis;

    @Column(name = "treatment_phase_1", columnDefinition = "TEXT")
    private String treatmentPhase1;

    @Column(name = "treatment_phase_2", columnDefinition = "TEXT")
    private String treatmentPhase2;

    @Column(name = "treatment_phase_3", columnDefinition = "TEXT")
    private String treatmentPhase3;

    @Column(name = "consent_accepted", nullable = false)
    private boolean consentAccepted;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected ClinicalHistory() {
    }

    public ClinicalHistory(
            String id,
            Patient patient,
            String birthDate,
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
            String treatmentPhase1,
            String treatmentPhase2,
            String treatmentPhase3,
            boolean consentAccepted,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.patient = patient;
        this.gender = gender;
        this.bloodType = bloodType;
        this.maritalStatus = maritalStatus;
        this.occupation = occupation;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.consultationReasons = consultationReasons;
        this.otherConsultationReason = otherConsultationReason;
        this.familyHistory = familyHistory;
        this.familyHistoryNotes = familyHistoryNotes;
        this.pathologicalHistory = pathologicalHistory;
        this.medicationAllergies = medicationAllergies;
        this.foodLatexAllergies = foodLatexAllergies;
        this.medications = medications;
        this.vaccinationStatus = vaccinationStatus;
        this.lastVaccine = lastVaccine;
        this.bisphosphonates = bisphosphonates;
        this.previousDentalAnesthesia = previousDentalAnesthesia;
        this.anesthesiaReaction = anesthesiaReaction;
        this.lastDentalTreatmentDate = lastDentalTreatmentDate;
        this.lastDentalTreatment = lastDentalTreatment;
        this.smoking = smoking;
        this.alcohol = alcohol;
        this.drugs = drugs;
        this.exercise = exercise;
        this.sleepHours = sleepHours;
        this.diet = diet;
        this.pregnancy = pregnancy;
        this.breastfeeding = breastfeeding;
        this.systems = systems;
        this.systemsNotes = systemsNotes;
        this.hygieneHabits = hygieneHabits;
        this.parafunctions = parafunctions;
        this.extraoralFindings = extraoralFindings;
        this.intraoralFindings = intraoralFindings;
        this.explorationNotes = explorationNotes;
        this.complementaryStudies = complementaryStudies;
        this.mainDiagnosis = mainDiagnosis;
        this.secondaryDiagnosis = secondaryDiagnosis;
        this.prognosis = prognosis;
        this.treatmentPhase1 = treatmentPhase1;
        this.treatmentPhase2 = treatmentPhase2;
        this.treatmentPhase3 = treatmentPhase3;
        this.consentAccepted = consentAccepted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodType(){
        return bloodType;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public Set<String> getConsultationReasons() {
        return consultationReasons;
    }

    public String getOtherConsultationReason() {
        return otherConsultationReason;
    }

    public Map<String, Boolean> getFamilyHistory() {
        return familyHistory;
    }

    public String getFamilyHistoryNotes() {
        return familyHistoryNotes;
    }

    public Map<String, Boolean> getPathologicalHistory() {
        return pathologicalHistory;
    }

    public String getMedicationAllergies() {
        return medicationAllergies;
    }

    public String getFoodLatexAllergies() {
        return foodLatexAllergies;
    }

    public List<MedicationData> getMedications() {
        return medications;
    }

    public String getVaccinationStatus() {
        return vaccinationStatus;
    }

    public String getLastVaccine() {
        return lastVaccine;
    }

    public Boolean getBisphosphonates() {
        return bisphosphonates;
    }

    public Boolean getPreviousDentalAnesthesia() {
        return previousDentalAnesthesia;
    }

    public String getAnesthesiaReaction() {
        return anesthesiaReaction;
    }

    public String getLastDentalTreatmentDate() {
        return lastDentalTreatmentDate;
    }

    public String getLastDentalTreatment() {
        return lastDentalTreatment;
    }

    public String getSmoking() {
        return smoking;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public String getDrugs() {
        return drugs;
    }

    public String getExercise() {
        return exercise;
    }

    public String getSleepHours() {
        return sleepHours;
    }

    public String getDiet() {
        return diet;
    }

    public String getPregnancy() {
        return pregnancy;
    }

    public String getBreastfeeding() {
        return breastfeeding;
    }

    public Map<String, Set<String>> getSystems() {
        return systems;
    }

    public String getSystemsNotes() {
        return systemsNotes;
    }

    public Set<String> getHygieneHabits() {
        return hygieneHabits;
    }

    public Set<String> getParafunctions() {
        return parafunctions;
    }

    public Set<String> getExtraoralFindings() {
        return extraoralFindings;
    }

    public Set<String> getIntraoralFindings() {
        return intraoralFindings;
    }

    public String getExplorationNotes() {
        return explorationNotes;
    }

    public String getComplementaryStudies() {
        return complementaryStudies;
    }

    public String getMainDiagnosis() {
        return mainDiagnosis;
    }

    public String getSecondaryDiagnosis() {
        return secondaryDiagnosis;
    }

    public String getPrognosis() {
        return prognosis;
    }

    public String getTreatmentPhase1() {
        return treatmentPhase1;
    }

    public String getTreatmentPhase2() {
        return treatmentPhase2;
    }

    public String getTreatmentPhase3() {
        return treatmentPhase3;
    }

    public boolean isConsentAccepted() {
        return consentAccepted;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(
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
            String treatmentPhase1,
            String treatmentPhase2,
            String treatmentPhase3,
            boolean consentAccepted,
            OffsetDateTime updatedAt) {
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.occupation = occupation;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.consultationReasons = consultationReasons;
        this.otherConsultationReason = otherConsultationReason;
        this.familyHistory = familyHistory;
        this.familyHistoryNotes = familyHistoryNotes;
        this.pathologicalHistory = pathologicalHistory;
        this.medicationAllergies = medicationAllergies;
        this.foodLatexAllergies = foodLatexAllergies;
        this.medications = medications;
        this.vaccinationStatus = vaccinationStatus;
        this.lastVaccine = lastVaccine;
        this.bisphosphonates = bisphosphonates;
        this.previousDentalAnesthesia = previousDentalAnesthesia;
        this.anesthesiaReaction = anesthesiaReaction;
        this.lastDentalTreatmentDate = lastDentalTreatmentDate;
        this.lastDentalTreatment = lastDentalTreatment;
        this.smoking = smoking;
        this.alcohol = alcohol;
        this.drugs = drugs;
        this.exercise = exercise;
        this.sleepHours = sleepHours;
        this.diet = diet;
        this.pregnancy = pregnancy;
        this.breastfeeding = breastfeeding;
        this.systems = systems;
        this.systemsNotes = systemsNotes;
        this.hygieneHabits = hygieneHabits;
        this.parafunctions = parafunctions;
        this.extraoralFindings = extraoralFindings;
        this.intraoralFindings = intraoralFindings;
        this.explorationNotes = explorationNotes;
        this.complementaryStudies = complementaryStudies;
        this.mainDiagnosis = mainDiagnosis;
        this.secondaryDiagnosis = secondaryDiagnosis;
        this.prognosis = prognosis;
        this.treatmentPhase1 = treatmentPhase1;
        this.treatmentPhase2 = treatmentPhase2;
        this.treatmentPhase3 = treatmentPhase3;
        this.consentAccepted = consentAccepted;
        this.updatedAt = updatedAt;
    }
}
