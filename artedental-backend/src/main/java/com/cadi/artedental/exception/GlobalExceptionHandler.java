package com.cadi.artedental.exception;
import java.time.Clock;
import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // =========================================================
    // RESOURCE NOT FOUND
    // =========================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
        ResourceNotFoundException exception
    ) {
        return buildResponse(
            HttpStatus.NOT_FOUND,
            exception.getMessage()
        );
    }

    // =========================================================
    // BUSINESS RULE
    // =========================================================

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiError> handleBusinessRule(
        BusinessRuleException exception
    ) {
        return buildResponse(
            HttpStatus.CONFLICT,
            exception.getMessage()
        );
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
        MethodArgumentNotValidException exception
    ) {
        String message = exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(
                error ->
                    error.getField()
                        + ": "
                        + error.getDefaultMessage()
            )
            .orElse(
                "La solicitud contiene datos inválidos."
            );

        return buildResponse(
            HttpStatus.BAD_REQUEST,
            message
        );
    }

    // =========================================================
    // ILLEGAL ARGUMENT
    // =========================================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
        IllegalArgumentException exception
    ) {
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            exception.getMessage()
        );
    }

    // =========================================================
    // GENERAL ERROR
    // =========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(
        Exception exception
    ) {
        return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocurrió un error interno en el servidor."
        );
    }

    // =========================================================
    // RESPONSE BUILDER
    // =========================================================

    private ResponseEntity<ApiError> buildResponse(
        HttpStatus status,
        String message
    ) {
        ApiError error = new ApiError(
            OffsetDateTime.now(),
            status.value(),
            status.getReasonPhrase(),
            message
        );

        return ResponseEntity
            .status(status)
            .body(error);
    }
}