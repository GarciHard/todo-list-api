package com.garcihard.todolist.util;

import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.model.entity.User;
import com.garcihard.todolist.security.CustomUserDetails;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class DefaultTestData {

    public static final String FORBIDDEN_CODE = "NOT_FOUND";
    public static final String FORBIDDEN_MESSAGE = "Task not found.";

    public static final UUID USER_ID = UUID.fromString("7e47e400-8d92-41a7-8ec5-bb29169047ba");
    public static final UUID TASK_ID = UUID.fromString("d495cde1-41d5-49b1-9d68-a9b94c5f5633");
    public static final String VALID_TOKEN = "valid-jwt-token";
    public static final String TASK_TITLE = "Title for test task";
    public static final String TASK_DESCRIPTION = "Just implementing testing";
    public static final boolean TASK_COMPLETED = false;
    public static final Instant FIXED_TIMESTAMP = Instant.parse("2025-06-23T00:00:00Z");
    public static final String USERNAME = "validUser";
    public static final String PASSWORD = "hashedPassword";


    public static Task getDefaultTask(UUID userId) {
        Task defaultTask =  new Task();
        defaultTask.setId(TASK_ID);
        defaultTask.setTitle(TASK_TITLE);
        defaultTask.setDescription(TASK_DESCRIPTION);
        defaultTask.setCompleted(TASK_COMPLETED);
        defaultTask.setUser(getDefaultUser(userId));
        defaultTask.setCreatedAt(FIXED_TIMESTAMP);
        defaultTask.setUpdatedAt(FIXED_TIMESTAMP);
        return defaultTask;
    }

    public static User getDefaultUser(UUID userId) {
        User defaultUser = new User();
        defaultUser.setId(userId);
        defaultUser.setUsername(USERNAME);
        defaultUser.setPassword(PASSWORD);
        defaultUser.setCreatedAt(FIXED_TIMESTAMP);
        defaultUser.setUpdatedAt(FIXED_TIMESTAMP);
        return defaultUser;
    }

    public static User getDefaultUserReference(UUID userId){
        User userReference = new User();
        userReference.setId(userId);
        return userReference;
    }

    public static TaskResponseDTO getDefaultTaskResponseDto(UUID taskId) {
        String taskIdStr = String.valueOf(taskId);
        return new TaskResponseDTO(
                taskIdStr, TASK_TITLE, TASK_DESCRIPTION, TASK_COMPLETED, FIXED_TIMESTAMP, FIXED_TIMESTAMP
        );
    }

    public static TaskRequestDTO getDefaultTaskRequestDto(UUID userId) {
        return new TaskRequestDTO(TASK_TITLE, TASK_DESCRIPTION, userId);
    }

    public static TaskUpdateDTO getDefaultTaskUpdateDto() {
        return new TaskUpdateDTO(TASK_TITLE, TASK_DESCRIPTION, TASK_COMPLETED);
    }

    public static CustomUserDetails getDefaultUserDetails() {
        return new CustomUserDetails(USER_ID, USERNAME, PASSWORD, List.of());
    }
}
