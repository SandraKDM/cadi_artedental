package com.cadi.artedental.treatment.payment.repository;

import com.cadi.artedental.treatment.payment.model.TreatmentPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TreatmentPaymentRepository
    extends JpaRepository<TreatmentPayment, UUID> {

    List<TreatmentPayment>
        findByTreatment_IdOrderByPaymentDateDesc(
            String treatmentId
        );

    List<TreatmentPayment>
        findByTreatment_Patient_IdOrderByPaymentDateDesc(
            String patientId
        );
}