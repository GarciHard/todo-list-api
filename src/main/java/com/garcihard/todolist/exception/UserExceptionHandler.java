package com.garcihard.todolist.exception;

import com.garcihard.todolist.exception.dto.ErrorResponse;
import com.garcihard.todolist.exception.user.ForbiddenResourceForLoggedUserException;
import com.garcihard.todolist.exception.user.UsernameAlreadyExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UsernameAlreadyExistException ex) {
        String errorCode = ex.getErrorCode();
        String message = ex.getMessage();

        log.warn("Handled UserAlreadyExistException [{}]: {}", errorCode, message);

        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, null);

        return ResponseEntity.status(httpStatus).body(errorResponse);
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
