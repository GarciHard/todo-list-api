package com.garcihard.todolist.exception.user;

import lombok.Getter;

@Getter
public class ForbiddenResourceForLoggedUserException extends RuntimeException {

    private final String errorCode;

    public ForbiddenResourceForLoggedUserException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
