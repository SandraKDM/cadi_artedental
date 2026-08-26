package com.cadi.artedental.treatment.dto.response;

import com.cadi.artedental.patient.odontogram.model.PatientTooth;
import com.cadi.artedental.treatment.model.Treatment;
import com.cadi.artedental.treatment.model.TreatmentStatus;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TreatmentResponse(

    String id,

    String patientId,

    String patientName,

    String toothNumber,

    String toothName,

    String treatmentName,

    String description,

    BigDecimal cost,

    BigDecimal amountPaid,

    BigDecimal progress,

    TreatmentStatus status,

    String notes,

    OffsetDateTime startDate,

    OffsetDateTime nextAppointment,

    OffsetDateTime completedDate,

    OffsetDateTime createdAt,

    OffsetDateTime updatedAt

) {

    public static TreatmentResponse fromEntity(
        Treatment treatment
    ) {
        PatientTooth patientTooth =
            treatment.getPatientTooth();

        String toothNumber = null;
        String toothName = null;

        if (patientTooth != null) {
            toothNumber =
                patientTooth.getToothNumber();

            toothName =
                getToothName(toothNumber);
        }

        return new TreatmentResponse(
            treatment.getId(),

            treatment
                .getPatient()
                .getId(),

            getPatientName(treatment),

            toothNumber,

            toothName,

            treatment.getTreatmentName(),

            treatment.getDescription(),

            treatment.getCost(),

            treatment.getAmountPaid(),

            treatment.getProgress(),

            treatment.getStatus(),

            treatment.getNotes(),

            treatment.getStartDate(),

            treatment.getNextAppointment(),

            treatment.getCompletedDate(),

            treatment.getCreatedAt(),

            treatment.getUpdatedAt()
        );
    }

    private static String getPatientName(
        Treatment treatment
    ) {
        var patient = treatment.getPatient();

        return String.join(
            " ",
            safe(patient.getNames()),
            safe(patient.getFirstName()),
            safe(patient.getLastName())
        )
        .replaceAll("\\s+", " ")
        .trim();
    }

    private static String safe(
        String value
    ) {
        return value == null
            ? ""
            : value.trim();
    }

    private static String getToothName(
        String toothNumber
    ) {
        if (toothNumber == null ||
            toothNumber.isBlank()) {
            return null;
        }

        return switch (toothNumber) {
            case "11" ->
                "Incisivo central superior derecho";
            case "12" ->
                "Incisivo lateral superior derecho";
            case "13" ->
                "Canino superior derecho";
            case "14" ->
                "Primer premolar superior derecho";
            case "15" ->
                "Segundo premolar superior derecho";
            case "16" ->
                "Primer molar superior derecho";
            case "17" ->
                "Segundo molar superior derecho";
            case "18" ->
                "Tercer molar superior derecho";

            case "21" ->
                "Incisivo central superior izquierdo";
            case "22" ->
                "Incisivo lateral superior izquierdo";
            case "23" ->
                "Canino superior izquierdo";
            case "24" ->
                "Primer premolar superior izquierdo";
            case "25" ->
                "Segundo premolar superior izquierdo";
            case "26" ->
                "Primer molar superior izquierdo";
            case "27" ->
                "Segundo molar superior izquierdo";
            case "28" ->
                "Tercer molar superior izquierdo";

            case "31" ->
                "Incisivo central inferior izquierdo";
            case "32" ->
                "Incisivo lateral inferior izquierdo";
            case "33" ->
                "Canino inferior izquierdo";
            case "34" ->
                "Primer premolar inferior izquierdo";
            case "35" ->
                "Segundo premolar inferior izquierdo";
            case "36" ->
                "Primer molar inferior izquierdo";
            case "37" ->
                "Segundo molar inferior izquierdo";
            case "38" ->
                "Tercer molar inferior izquierdo";

            case "41" ->
                "Incisivo central inferior derecho";
            case "42" ->
                "Incisivo lateral inferior derecho";
            case "43" ->
                "Canino inferior derecho";
            case "44" ->
                "Primer premolar inferior derecho";
            case "45" ->
                "Segundo premolar inferior derecho";
            case "46" ->
                "Primer molar inferior derecho";
            case "47" ->
                "Segundo molar inferior derecho";
            case "48" ->
                "Tercer molar inferior derecho";

            default ->
                "Diente " + toothNumber;
        };
    }
}