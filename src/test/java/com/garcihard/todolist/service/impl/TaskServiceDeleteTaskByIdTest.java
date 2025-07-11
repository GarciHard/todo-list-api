package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.exception.custom.ForbiddenResourceForLoggedUserException;
import com.garcihard.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.garcihard.todolist.util.DefaultTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceDeleteTaskByIdTest {

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskServiceImpl taskService;

    private UUID userId;
    private UUID taskId;

    @BeforeEach
    void setupTestData() {
        userId = USER_ID;
        taskId = TASK_ID;
    }

    @Test
    void shouldDeleteTaskWhenUserIdAndTaskIdAreValid() {
        when(taskRepository.deleteByTaskIdAndUserId(taskId, userId)).thenReturn(1);

        taskService.deleteUserTaskById(userId, taskId);

        verify(taskRepository, times(1)).deleteByTaskIdAndUserId(taskId, userId);
    }

    @Test
    void shouldThrowForbiddenResourceForLoggedUserWhenUserDeleteUnauthorizedTask() {
        when(taskRepository.deleteByTaskIdAndUserId(taskId, userId)).thenReturn(0);

        assertThrows(ForbiddenResourceForLoggedUserException.class, () -> {
            taskService.deleteUserTaskById(userId, taskId);
        });

        verify(taskRepository, times(1)).deleteByTaskIdAndUserId(taskId, userId);
    }
}