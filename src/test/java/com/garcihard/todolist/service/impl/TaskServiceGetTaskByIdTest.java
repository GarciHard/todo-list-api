package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.exception.user.ForbiddenResourceForLoggedUserException;
import com.garcihard.todolist.mapper.TaskMapper;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.repository.TaskRepository;
import com.garcihard.todolist.security.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.garcihard.todolist.util.DefaultTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceGetTaskByIdTest {

    @Mock
    JwtUtil jwtUtil;

    @Mock
    TaskMapper mapper;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskServiceImpl taskService;

    private UUID userId;
    private UUID taskId;
    private TaskResponseDTO taskResponse;
    private Task task;

    @BeforeEach
    void setupTestData() {
        userId = USER_ID;
        taskId = TASK_ID;

        task = getDefaultTask(userId);
        taskResponse = getDefaultTaskResponseDto(userId);
    }

    @Test
    void shouldReturnTaskDtoWhenTokenAndTaskIdAreValid() {
        when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn(userId);
        when(taskRepository.findByIdAndUserId(taskId, userId)).thenReturn(Optional.ofNullable(task));
        when(mapper.toResponseDto(task)).thenReturn(taskResponse);

        TaskResponseDTO result = taskService.getUserTaskById(VALID_TOKEN, taskId);

        assertThat(result.id()).isEqualTo(taskResponse.id());
        assertThat(result.title()).isEqualTo(taskResponse.title());
        assertThat(result.description()).isEqualTo(taskResponse.description());
        assertThat(result.completed()).isEqualTo(taskResponse.completed());
        assertThat(result.createdAt()).isEqualTo(taskResponse.createdAt());
        assertThat(result.updatedAt()).isEqualTo(taskResponse.updatedAt());

        verify(jwtUtil, times(1)).extractUserId(VALID_TOKEN);
        verify(taskRepository, times(1)).findByIdAndUserId(taskId, userId);
        verify(mapper, times(1)).toResponseDto(task);
    }

    @Test
    void shouldThrowForbiddenResourceForLoggedUserWhenUserRetrieveUnauthorizedTask() {
        when(jwtUtil.extractUserId(VALID_TOKEN)).thenReturn(userId);
        when(taskRepository.findByIdAndUserId(taskId, userId))
                .thenThrow(new ForbiddenResourceForLoggedUserException(FORBIDDEN_CODE, FORBIDDEN_MESSAGE));

        assertThatThrownBy(() -> taskService.getUserTaskById(VALID_TOKEN, taskId))
                .isInstanceOf(ForbiddenResourceForLoggedUserException.class)
                .hasMessage(FORBIDDEN_MESSAGE);

        verify(jwtUtil, times(1)).extractUserId(VALID_TOKEN);
        verify(taskRepository, times(1)).findByIdAndUserId(taskId, userId);
        verify(mapper, times(0)).toResponseDto(any());
    }

}
