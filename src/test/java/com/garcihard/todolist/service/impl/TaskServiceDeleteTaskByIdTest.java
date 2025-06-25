package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.exception.user.ForbiddenResourceForLoggedUserException;
import com.garcihard.todolist.repository.TaskRepository;
import com.garcihard.todolist.security.util.JwtUtil;
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
    JwtUtil jwtUtil;

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
        when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn(userId);
        when(taskRepository.deleteByTaskIdAndUserId(taskId, userId)).thenReturn(1);

        taskService.deleteUserTaskById(VALID_TOKEN, taskId);

        verify(jwtUtil, times(1)).extractUserId(VALID_TOKEN);
        verify(taskRepository, times(1)).deleteByTaskIdAndUserId(taskId, userId);
    }

    @Test
    void shouldThrowForbiddenResourceForLoggedUserWhenUserDeleteUnauthorizedTask() {
        when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn(userId);
        when(taskRepository.deleteByTaskIdAndUserId(taskId, userId)).thenReturn(0);

        assertThrows(ForbiddenResourceForLoggedUserException.class, () -> {
            taskService.deleteUserTaskById(VALID_TOKEN, taskId);
        });

        verify(jwtUtil, times(1)).extractUserId(VALID_TOKEN);
        verify(taskRepository, times(1)).deleteByTaskIdAndUserId(taskId, userId);
    }
}
