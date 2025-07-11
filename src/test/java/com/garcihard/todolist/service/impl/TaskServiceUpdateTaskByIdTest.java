package com.garcihard.todolist.service.impl;

import com.garcihard.todolist.exception.custom.ForbiddenResourceForLoggedUserException;
import com.garcihard.todolist.mapper.TaskMapper;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.model.entity.Task;
import com.garcihard.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.garcihard.todolist.util.DefaultTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUpdateTaskByIdTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    TaskServiceImpl taskService;

    private UUID userId;
    private UUID taskId;
    private Task task;
    private TaskUpdateDTO taskUpdateDto;
    private TaskResponseDTO taskResponseDto;

    @BeforeEach
    void setupTestData() {
        userId = USER_ID;
        taskId = TASK_ID;

        task = getDefaultTask(userId);
        taskUpdateDto = getDefaultTaskUpdateDto();
        taskResponseDto = getDefaultTaskResponseDto(taskId);
    }

    @Test
    void shouldUpdateTaskAndReturnDtoWhenTokenAndTaskIdAndTaskRequestAreValid() {
        when(taskRepository.findByIdAndUserId(taskId, userId)).thenReturn(Optional.ofNullable(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(mapper.toResponseDto(task)).thenReturn(taskResponseDto);

        TaskResponseDTO result = taskService.updateUserTaskById(userId, taskId, taskUpdateDto);

        assertThat(result.title()).isEqualTo(taskUpdateDto.title());
        assertThat(result.description()).isEqualTo(taskUpdateDto.description());
        assertThat(result.completed()).isEqualTo(taskUpdateDto.completed());

        assertThat(result.id()).isEqualTo(task.getId().toString());
        assertThat(result.title()).isEqualTo(task.getTitle());
        assertThat(result.description()).isEqualTo(task.getDescription());
        assertThat(result.completed()).isEqualTo(task.isCompleted());
        assertThat(result.createdAt()).isEqualTo(task.getCreatedAt());
        assertThat(result.updatedAt()).isEqualTo(task.getUpdatedAt());

        verify(taskRepository, times(1)).findByIdAndUserId(taskId, userId);
        verify(taskRepository, times(1)).save(task);
        verify(mapper, times(1)).toResponseDto(task);
    }

    @Test
    void shouldThrowForbiddenResourceForLoggedUserWhenUserUpdateUnauthorizedTask() {
        when(taskRepository.findByIdAndUserId(taskId, userId))
                .thenThrow(new ForbiddenResourceForLoggedUserException(FORBIDDEN_CODE, FORBIDDEN_MESSAGE));

        assertThatThrownBy(() -> taskService.updateUserTaskById(userId, taskId, taskUpdateDto))
                .isInstanceOf(ForbiddenResourceForLoggedUserException.class)
                .hasMessage(FORBIDDEN_MESSAGE);

        verify(taskRepository, times(1)).findByIdAndUserId(taskId, userId);
    }
}