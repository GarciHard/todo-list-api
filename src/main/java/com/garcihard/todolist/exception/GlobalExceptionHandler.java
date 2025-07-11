package com.garcihard.todolist.exception;

import com.garcihard.todolist.exception.dto.ErrorResponse;
import com.garcihard.todolist.exception.dto.FieldErrorResponse;
import com.garcihard.todolist.exception.custom.ForbiddenResourceForLoggedUserException;
import com.garcihard.todolist.util.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorCode = ApiConstants.REQUEST_VALIDATION_CODE;
        String message = ApiConstants.REQUEST_VALIDATION_FAILED;

        List<FieldErrorResponse> fieldErrorResponses = ex.getBindingResult().getFieldErrors()
                .stream().map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        log.warn("Handled ValidationException [{}]: {}", errorCode, message);

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, fieldErrorResponses);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenResourceForLoggedUserException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenResourceForLoggedUserException(
            ForbiddenResourceForLoggedUserException ex) {
        String errorCode = ex.getErrorCode();
        String message = ex.getMessage();

        log.warn("Handled ForbiddenResourceForLoggedUserException [{}]: {}", errorCode, message);

        HttpStatus responseStatus = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, null);

        return ResponseEntity.status(responseStatus).body(errorResponse);
    }
}
