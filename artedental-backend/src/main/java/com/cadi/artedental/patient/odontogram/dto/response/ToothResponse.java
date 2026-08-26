package com.cadi.artedental.patient.odontogram.dto.response;

import com.cadi.artedental.patient.odontogram.model.PatientTooth;
import com.cadi.artedental.patient.odontogram.model.ToothStatus;

import java.time.OffsetDateTime;

public record ToothResponse(

    String id,
    String patientId,
    String toothNumber,
    String dentitionType,
    String toothType,
    ToothStatus status,
    String diagnosis,
    String notes,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt

) {

    public static ToothResponse fromEntity(
        PatientTooth tooth
    ) {
        return new ToothResponse(
            tooth.getId(),
            tooth.getPatient().getId(),
            tooth.getToothNumber(),
            tooth.getDentitionType(),
            tooth.getToothType(),
            tooth.getStatus(),
            tooth.getDiagnosis(),
            tooth.getNotes(),
            tooth.getCreatedAt(),
            tooth.getUpdatedAt()
        );
    }
}