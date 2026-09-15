package com.cadi.artedental.billing.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cadi.artedental.billing.dto.CreateInvoiceRequest;
import com.cadi.artedental.billing.dto.InvoiceRequestResponse;
import com.cadi.artedental.billing.service.InvoiceRequestService;

@RestController
@RequestMapping("/api/patients/{patientId}/invoice-requests")
public class InvoiceRequestController {

        private final InvoiceRequestService invoiceRequestService;

        public InvoiceRequestController(
                        InvoiceRequestService invoiceRequestService) {
                this.invoiceRequestService = invoiceRequestService;
        }

        // =========================================================
        // CREATE
        // =========================================================

        @PostMapping
        public ResponseEntity<InvoiceRequestResponse> create(
                        @PathVariable String patientId,
                        @RequestBody CreateInvoiceRequest request) {

                InvoiceRequestResponse response = invoiceRequestService.create(
                                patientId,
                                request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        // =========================================================
        // HISTORY
        // =========================================================

        @GetMapping
        public ResponseEntity<List<InvoiceRequestResponse>> findByPatientId(
                        @PathVariable String patientId) {

                return ResponseEntity.ok(
                                invoiceRequestService
                                                .findByPatientId(
                                                                patientId));
        }

        @PostMapping("/{invoiceRequestId}/retry")
        public ResponseEntity<InvoiceRequestResponse> retry(
                        @PathVariable String patientId,
                        @PathVariable String invoiceRequestId) {

                return ResponseEntity.ok(
                                invoiceRequestService.retrySend(
                                                patientId,
                                                invoiceRequestId));
        }

        @PostMapping("/{invoiceRequestId}/process")
        public ResponseEntity<InvoiceRequestResponse> process(
                        @PathVariable String patientId,
                        @PathVariable String invoiceRequestId) {

                return ResponseEntity.ok(
                                invoiceRequestService.markAsProcessed(
                                                patientId,
                                                invoiceRequestId));
        }

        @PostMapping("/{invoiceRequestId}/cancel")
        public ResponseEntity<InvoiceRequestResponse> cancel(
                        @PathVariable String patientId,
                        @PathVariable String invoiceRequestId) {

                return ResponseEntity.ok(
                                invoiceRequestService.cancel(
                                                patientId,
                                                invoiceRequestId));
        }
}