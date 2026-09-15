package com.cadi.artedental.billing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cadi.artedental.billing.model.InvoiceRequest;

@Service
public class BillingEmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${artedental.billing.clinic-name:Consultorio Arte Dental}")
    private String clinicName;

    public BillingEmailService(
            JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendInvoiceRequest(
            InvoiceRequest request) {

        if (fromEmail == null ||
                fromEmail.isBlank()) {

            throw new IllegalStateException(
                    "No está configurado spring.mail.username.");
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);

        message.setTo(
                request.getAccountantEmail());

        message.setReplyTo(
                request.getBillingEmail());

        message.setSubject(
                "Solicitud de facturación - "
                        + clinicName);

        message.setText(
                buildBody(request));

        mailSender.send(message);
    }

    private String buildBody(
            InvoiceRequest request) {

        return """
                SOLICITUD DE FACTURACIÓN

                DATOS FISCALES
                RFC: %s
                Razón social: %s
                Régimen fiscal: %s
                Código postal: %s
                Uso CFDI: %s
                Correo de facturación: %s

                CONCEPTO
                Tipo: %s
                Descripción: %s
                Monto: $%s

                DATOS DE ENVÍO
                Contador: %s
                Folio interno: %s
                Fecha: %s

                Notas:
                %s
                """.formatted(
                request.getRfc(),
                request.getBusinessName(),
                request.getTaxRegime(),
                request.getFiscalZipCode(),
                request.getCfdiUse(),
                request.getBillingEmail(),
                request.getItemType(),
                request.getDescription(),
                request.getAmount(),
                request.getAccountantEmail(),
                request.getId(),
                request.getRequestedAt(),
                request.getNotes() == null
                        ? ""
                        : request.getNotes());
    }

    private String getItemTypeLabel(
            com.cadi.artedental.billing.model.InvoiceItemType type) {

        return switch (type) {

            case APPOINTMENT ->
                "Cita";

            case TREATMENT ->
                "Tratamiento completo";

            case TREATMENT_PAYMENT ->
                "Pago de tratamiento";
        };
    }
}