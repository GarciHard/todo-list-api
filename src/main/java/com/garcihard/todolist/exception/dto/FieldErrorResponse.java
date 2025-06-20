package com.garcihard.todolist.exception.dto;

public record FieldErrorResponse(String field,
                                 String message) {
}
