package com.garcihard.todolist.exception;

import com.garcihard.todolist.exception.dto.ErrorResponse;
import com.garcihard.todolist.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.garcihard.todolist.util.UserConstants;

@Slf4j
@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
        String errorCode = ex.getErrorCode();
        String message = ex.getMessage();

        log.warn("Handled UserException [{}]: {}", errorCode, message);

        HttpStatus httpStatus = mapToHttpStatus(errorCode);
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, null);

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private HttpStatus mapToHttpStatus(String errorCode) {
        return switch (errorCode) {
            case UserConstants.USER_CONFLICT_CODE -> HttpStatus.CONFLICT;
            case UserConstants.USER_NOT_FOUND_CODE ->  HttpStatus.NOT_FOUND;
            default -> HttpStatus.I_AM_A_TEAPOT;
        };
    }
}
