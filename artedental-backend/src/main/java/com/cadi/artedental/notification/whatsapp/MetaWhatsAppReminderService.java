package com.cadi.artedental.notification.whatsapp;

import com.cadi.artedental.agenda.model.AgendaAppointment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Primary
public class MetaWhatsAppReminderService
    implements WhatsAppReminderService {

    private final RestClient restClient;

    private final String apiVersion;
    private final String phoneNumberId;
    private final String accessToken;
    private final String templateName;
    private final String templateLanguage;
    private final String clinicName;

    private final Clock clock;

    public MetaWhatsAppReminderService(
        RestClient.Builder restClientBuilder,

        Clock clock,

        @Value("${whatsapp.api.base-url}")
        String baseUrl,

        @Value("${whatsapp.api.version}")
        String apiVersion,

        @Value("${whatsapp.phone-number-id}")
        String phoneNumberId,

        @Value("${whatsapp.access-token}")
        String accessToken,

        @Value("${whatsapp.template-name}")
        String templateName,

        @Value("${whatsapp.template-language}")
        String templateLanguage,

        @Value("${app.clinic-name}")
        String clinicName
    ) {

        this.restClient =
            restClientBuilder
                .baseUrl(baseUrl)
                .build();

        this.clock = clock;

        this.apiVersion = apiVersion;
        this.phoneNumberId = phoneNumberId;
        this.accessToken = accessToken;
        this.templateName = templateName;
        this.templateLanguage =
            templateLanguage;
        this.clinicName = clinicName;
    }

    @Override
    public void sendAppointmentReminder(
        AgendaAppointment appointment
    ) {

        String phone =
            normalizePhone(
                appointment.getContactPhone()
            );

        ZonedDateTime appointmentDate =
            appointment
                .getStartDateTime()
                .atZoneSameInstant(
                    clock.getZone()
                );

        String patientName =
            appointment.getContactName();

        String formattedDate =
            formatDate(
                appointmentDate
            );

        String formattedTime =
            formatTime(
                appointmentDate
            );

        String reason =
            appointment.getReason();

        Map<String, Object> body =
            buildTemplateBody(
                phone,
                patientName,
                formattedDate,
                formattedTime,
                reason
            );

        restClient
            .post()
            .uri(
                "/{version}/{phoneNumberId}/messages",
                apiVersion,
                phoneNumberId
            )
            .header(
                HttpHeaders.AUTHORIZATION,
                "Bearer " + accessToken
            )
            .contentType(
                MediaType.APPLICATION_JSON
            )
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }

    private Map<String, Object> buildTemplateBody(
        String phone,
        String patientName,
        String formattedDate,
        String formattedTime,
        String reason
    ) {

        List<Map<String, Object>> parameters =
            List.of(
                textParameter(patientName),
                textParameter(clinicName),
                textParameter(formattedDate),
                textParameter(formattedTime),
                textParameter(reason)
            );

        Map<String, Object> bodyComponent =
            Map.of(
                "type",
                "body",

                "parameters",
                parameters
            );

        Map<String, Object> template =
            Map.of(
                "name",
                templateName,

                "language",
                Map.of(
                    "code",
                    templateLanguage
                ),

                "components",
                List.of(
                    bodyComponent
                )
            );

        return Map.of(
            "messaging_product",
            "whatsapp",

            "to",
            phone,

            "type",
            "template",

            "template",
            template
        );
    }

    private Map<String, Object> textParameter(
        String value
    ) {

        return Map.of(
            "type",
            "text",

            "text",
            value
        );
    }

    private String formatDate(
        ZonedDateTime date
    ) {

        Locale locale =
            Locale.forLanguageTag(
                "es-MX"
            );

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                "d 'de' MMMM 'de' yyyy",
                locale
            );

        return date.format(formatter);
    }

    private String formatTime(
        ZonedDateTime date
    ) {

        Locale locale =
            Locale.forLanguageTag(
                "es-MX"
            );

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern(
                "h:mm a",
                locale
            );

        return date
            .format(formatter)
            .toLowerCase(locale);
    }

    private String normalizePhone(
        String phone
    ) {

        String digits =
            phone.replaceAll(
                "\\D",
                ""
            );

        // México:
        // 5576629626
        // ↓
        // 525576629626

        if (digits.length() == 10) {
            return "52" + digits;
        }

        // Formato mexicano antiguo:
        // 5215576629626
        // ↓
        // 525576629626

        if (
            digits.length() == 13
            && digits.startsWith("521")
        ) {

            return "52"
                + digits.substring(3);
        }

        return digits;
    }
}