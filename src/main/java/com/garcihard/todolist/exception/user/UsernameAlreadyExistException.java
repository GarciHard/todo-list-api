package com.garcihard.todolist.exception.user;

import lombok.Getter;

@Getter
public class UsernameAlreadyExistException extends RuntimeException {

    private final String errorCode;

    public UsernameAlreadyExistException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
