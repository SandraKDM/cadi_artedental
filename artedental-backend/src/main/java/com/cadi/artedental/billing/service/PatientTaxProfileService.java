package com.cadi.artedental.billing.service;

import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cadi.artedental.billing.dto.PatientTaxProfileRequest;
import com.cadi.artedental.billing.dto.PatientTaxProfileResponse;
import com.cadi.artedental.billing.model.PatientTaxProfile;
import com.cadi.artedental.billing.repository.PatientTaxProfileRepository;
import com.cadi.artedental.patient.model.Patient;
import com.cadi.artedental.patient.repository.PatientRepository;

@Service
public class PatientTaxProfileService {

    private final PatientTaxProfileRepository
        taxProfileRepository;

    private final PatientRepository
        patientRepository;

    private static final Pattern RFC_PATTERN =
        Pattern.compile(
            "^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$"
        );

    private static final Pattern ZIP_CODE_PATTERN =
        Pattern.compile(
            "^[0-9]{5}$"
        );

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile(
            "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"
        );

    public PatientTaxProfileService(
        PatientTaxProfileRepository taxProfileRepository,
        PatientRepository patientRepository
    ) {
        this.taxProfileRepository =
            taxProfileRepository;

        this.patientRepository =
            patientRepository;
    }

    // =========================================================
    // GET
    // =========================================================

    @Transactional(readOnly = true)
    public PatientTaxProfileResponse getByPatientId(
        String patientId
    ) {

        PatientTaxProfile profile =
            taxProfileRepository
                .findByPatient_Id(patientId)
                .orElseThrow(
                    () -> new IllegalArgumentException(
                        "El paciente no tiene datos fiscales registrados."
                    )
                );

        return toResponse(profile);
    }

    // =========================================================
    // CREATE / UPDATE
    // =========================================================

    @Transactional
    public PatientTaxProfileResponse save(
        String patientId,
        PatientTaxProfileRequest request
    ) {

        // -----------------------------------------------------
        // 1. Validar paciente
        // -----------------------------------------------------

        Patient patient =
            patientRepository
                .findById(patientId)
                .orElseThrow(
                    () -> new IllegalArgumentException(
                        "No se encontró el paciente "
                            + patientId
                    )
                );

        // -----------------------------------------------------
        // 2. Normalizar y validar request
        // -----------------------------------------------------

        String rfc =
            normalizeRfc(request.getRfc());

        String businessName =
            normalizeRequired(
                request.getBusinessName(),
                "La razón social es obligatoria."
            );

        String taxRegime =
            normalizeRequired(
                request.getTaxRegime(),
                "El régimen fiscal es obligatorio."
            );

        String fiscalZipCode =
            normalizeRequired(
                request.getFiscalZipCode(),
                "El código postal fiscal es obligatorio."
            );

        String cfdiUse =
            normalizeRequired(
                request.getCfdiUse(),
                "El uso CFDI es obligatorio."
            );

        String billingEmail =
            normalizeRequired(
                request.getBillingEmail(),
                "El correo de facturación es obligatorio."
            ).toLowerCase(Locale.ROOT);

        validateRfc(rfc);

        validateFiscalZipCode(
            fiscalZipCode
        );

        validateEmail(
            billingEmail
        );

        // -----------------------------------------------------
        // 3. Buscar existente o crear uno nuevo
        // -----------------------------------------------------

        PatientTaxProfile profile =
            taxProfileRepository
                .findByPatient_Id(patientId)
                .orElseGet(
                    () -> {
                        PatientTaxProfile newProfile =
                            new PatientTaxProfile();

                        newProfile.setPatient(
                            patient
                        );

                        return newProfile;
                    }
                );

        // -----------------------------------------------------
        // 4. Actualizar datos fiscales
        // -----------------------------------------------------

        profile.setRfc(rfc);

        profile.setBusinessName(
            businessName
        );

        profile.setTaxRegime(
            taxRegime
        );

        profile.setFiscalZipCode(
            fiscalZipCode
        );

        profile.setCfdiUse(
            cfdiUse
        );

        profile.setBillingEmail(
            billingEmail
        );

        // -----------------------------------------------------
        // 5. Guardar
        // -----------------------------------------------------

        PatientTaxProfile saved =
            taxProfileRepository.save(
                profile
            );

        return toResponse(saved);
    }

    // =========================================================
    // MAPPER
    // =========================================================

    private PatientTaxProfileResponse toResponse(
        PatientTaxProfile profile
    ) {

        String patientId = null;

        if (profile.getPatient() != null) {
            patientId =
                profile
                    .getPatient()
                    .getId();
        }

        return new PatientTaxProfileResponse(
            profile.getId(),
            patientId,
            profile.getRfc(),
            profile.getBusinessName(),
            profile.getTaxRegime(),
            profile.getFiscalZipCode(),
            profile.getCfdiUse(),
            profile.getBillingEmail(),
            profile.getCreatedAt(),
            profile.getUpdatedAt()
        );
    }

    // =========================================================
    // NORMALIZATION
    // =========================================================

    private String normalizeRfc(
        String value
    ) {

        if (value == null ||
            value.trim().isEmpty()) {

            throw new IllegalArgumentException(
                "El RFC es obligatorio."
            );
        }

        return value
            .trim()
            .toUpperCase(Locale.ROOT);
    }

    private String normalizeRequired(
        String value,
        String errorMessage
    ) {

        if (value == null ||
            value.trim().isEmpty()) {

            throw new IllegalArgumentException(
                errorMessage
            );
        }

        return value.trim();
    }

    // =========================================================
    // VALIDATIONS
    // =========================================================

    private void validateRfc(
        String rfc
    ) {

        if (!RFC_PATTERN.matcher(rfc).matches()) {
            throw new IllegalArgumentException(
                "El RFC no tiene un formato válido."
            );
        }
    }

    private void validateFiscalZipCode(
        String zipCode
    ) {

        if (!ZIP_CODE_PATTERN
                .matcher(zipCode)
                .matches()) {

            throw new IllegalArgumentException(
                "El código postal fiscal debe contener 5 dígitos."
            );
        }
    }

    private void validateEmail(
        String email
    ) {

        if (!EMAIL_PATTERN
                .matcher(email)
                .matches()) {

            throw new IllegalArgumentException(
                "El correo de facturación no tiene un formato válido."
            );
        }
    }
}