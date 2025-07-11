package com.garcihard.todolist.exception.custom;

import lombok.Getter;

@Getter
public class ForbiddenResourceForLoggedUserException extends RuntimeException {

    private final String errorCode;

    public ForbiddenResourceForLoggedUserException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
