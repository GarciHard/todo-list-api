package com.garcihard.todolist.exception;

import com.garcihard.todolist.exception.dto.ErrorResponse;
import com.garcihard.todolist.exception.dto.FieldErrorResponse;
import com.garcihard.todolist.util.UserConstants;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String errorCode = "VALIDATION";
        String message = "Validation Failed for the given request.";

        List<FieldErrorResponse> fieldErrorResponses = ex.getBindingResult().getFieldErrors()
                .stream().map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        log.warn("Handled ValidationException [{}]: {}", errorCode, message);

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, fieldErrorResponses);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(BadCredentialsException ex) {
        String errorCode = UserConstants.INVALID_CREDENTIALS_CODE;
        String message = UserConstants.INVALID_CREDENTIALS;

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, null);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ClaimJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ClaimJwtException ex) {
        String errorCode = UserConstants.INVALID_CREDENTIALS_CODE;
        String message = "Jwt is expired.";

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
