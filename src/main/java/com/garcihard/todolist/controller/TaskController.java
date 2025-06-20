package com.garcihard.todolist.controller;

import com.garcihard.todolist.model.dto.TaskRequestDTO;
import com.garcihard.todolist.model.dto.TaskResponseDTO;
import com.garcihard.todolist.model.dto.TaskUpdateDTO;
import com.garcihard.todolist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(TaskController.BASE_URL)
@RestController
public class TaskController {

    static final String BASE_URL = "/api/v1/task";
    static final String TASK_BY_ID = "/{id}";
    static final String AUTH_HEADER = "Authorization";

    private final TaskService taskService;

    @GetMapping()
    public ResponseEntity<List<TaskResponseDTO>> getAllTaskFromUser(
            @RequestHeader(TaskController.AUTH_HEADER) String token) {
        List<TaskResponseDTO> response = taskService.listAllUserTask(token);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> addNewUserTask(
            @RequestHeader(TaskController.AUTH_HEADER) String token,
            @RequestBody TaskRequestDTO newTask) {
        TaskResponseDTO response = taskService.createUserTask(token, newTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @RequestHeader(TaskController.AUTH_HEADER) String token,
            @PathVariable("id") UUID taskId) {
        TaskResponseDTO response = taskService.getUserTaskById(token, taskId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<TaskResponseDTO> updateTaskById(
            @RequestHeader(TaskController.AUTH_HEADER) String token,
            @PathVariable("id") UUID taskId,
            @RequestBody TaskUpdateDTO updatedTask) {
        TaskResponseDTO response = taskService.updateUserTaskById(token, taskId, updatedTask);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(TaskController.TASK_BY_ID)
    public ResponseEntity<Void> deleteTaskById(
            @RequestHeader(TaskController.AUTH_HEADER) String token,
            @PathVariable("id") UUID taskId) {
        taskService.deleteUserTaskById(token, taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/health")
    public String test() {
        return "Hola";
    }
}
