package com.garcihard.todolist.service;

import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponseDTO> listUserTasks(CustomUserDetails userDetails);
    TaskResponseDTO createUserTask(CustomUserDetails userDetails, TaskRequestDTO task);
    TaskResponseDTO getUserTaskById(CustomUserDetails userDetails, UUID taskId);
    void deleteUserTaskById(CustomUserDetails userDetails, UUID taskId);
    TaskResponseDTO updateUserTaskById(CustomUserDetails userDetails, UUID taskId, TaskUpdateDTO updatedTask);
}
