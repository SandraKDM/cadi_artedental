package com.cadi.artedental.exception;
import java.time.OffsetDateTime;

public record ApiError(
    OffsetDateTime timestamp,
    int status,
    String error,
    String message
) {
}