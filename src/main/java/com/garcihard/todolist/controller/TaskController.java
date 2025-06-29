package com.garcihard.todolist.controller;

import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.security.CustomUserDetails;
import com.garcihard.todolist.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(TaskController.BASE_URL)
@RestController
public class TaskController {

    static final String BASE_URL = "/api/v1/task";
    static final String TASK_BY_ID = "/{id}";
    static final String ID = "id";

    private final TaskService taskService;

    @GetMapping()
    public ResponseEntity<List<TaskResponseDTO>> getAllTaskFromUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TaskResponseDTO> response = taskService.listUserTasks(userDetails);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> addNewUserTask(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid TaskRequestDTO newTask) {
        TaskResponseDTO response = taskService.createUserTask(userDetails, newTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(ID) UUID taskId) {
        TaskResponseDTO response = taskService.getUserTaskById(userDetails, taskId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> updateTaskById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(ID) UUID taskId,
            @RequestBody @Valid TaskUpdateDTO updatedTask) {
        TaskResponseDTO response = taskService.updateUserTaskById(userDetails, taskId, updatedTask);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<Void> deleteTaskById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable(ID) UUID taskId) {
        taskService.deleteUserTaskById(userDetails, taskId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
