package com.garcihard.todolist.model.dto;

import java.util.UUID;

public record TaskUpdateDTO(String title,
                            String description,
                            boolean completed) {
}
