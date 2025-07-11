package com.garcihard.todolist.service;

import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponseDTO> listUserTasks(UUID userId);
    TaskResponseDTO createUserTask(UUID userId, TaskRequestDTO task);
    TaskResponseDTO getUserTaskById(UUID userId, UUID taskId);
    void deleteUserTaskById(UUID userId, UUID taskId);
    TaskResponseDTO updateUserTaskById(UUID userId, UUID taskId, TaskUpdateDTO updatedTask);
}
