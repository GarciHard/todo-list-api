package com.garcihard.todolist.service;

import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponseDTO> listUserTasks(String token);
    TaskResponseDTO createUserTask(String token, TaskRequestDTO task);
    TaskResponseDTO getUserTaskById(String token, UUID taskId);
    void deleteUserTaskById(String token, UUID taskId);
    TaskResponseDTO updateUserTaskById(String token, UUID taskId, TaskUpdateDTO updatedTask);
}
