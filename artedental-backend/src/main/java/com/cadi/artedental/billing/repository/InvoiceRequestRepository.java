package com.cadi.artedental.billing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadi.artedental.billing.model.InvoiceItemType;
import com.cadi.artedental.billing.model.InvoiceRequest;
import com.cadi.artedental.billing.model.InvoiceRequestStatus;

@Repository
public interface InvoiceRequestRepository
    extends JpaRepository<InvoiceRequest, String> {

    List<InvoiceRequest>
        findByPatient_IdOrderByRequestedAtDesc(
            String patientId
        );

    List<InvoiceRequest>
        findByPatient_IdAndStatusNot(
            String patientId,
            InvoiceRequestStatus status
        );

    Optional<InvoiceRequest>
    findFirstByItemTypeAndReferenceIdAndStatusNot(
        InvoiceItemType itemType,
        String referenceId,
        InvoiceRequestStatus status
    );

    boolean existsByItemTypeAndReferenceIdAndStatusNot(
        InvoiceItemType itemType,
        String referenceId,
        InvoiceRequestStatus status
    );
}