package com.garcihard.todolist.exception.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserException extends RuntimeException {

    private final String errorCode;

    public UserException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public UserException(String errorCode, String message){
        this(errorCode, message, null);
    }
}
