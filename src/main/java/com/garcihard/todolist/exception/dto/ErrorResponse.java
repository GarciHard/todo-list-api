package com.garcihard.todolist.exception.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(String timestamp,
                            String errorCode,
                            String message,
                            List<FieldErrorResponse> fieldErrors) {

    public ErrorResponse(String errorCode, String message, List<FieldErrorResponse> fieldErrors) {
        this(Instant.now().toString(), errorCode, message, fieldErrors);
    }

    public static ErrorResponse of(String errorCode, String message, List<FieldErrorResponse> fieldErrors) {
        return new ErrorResponse(errorCode, message, fieldErrors);
    }
}
