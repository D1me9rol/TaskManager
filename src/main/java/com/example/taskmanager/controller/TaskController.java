package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskEventResponse;
import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.TaskStatusRequest;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.mapper.TaskEventMapper;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.model.TaskStatus;
import com.example.taskmanager.model.SortField;
import com.example.taskmanager.model.SortDirection;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskEventMapper taskEventMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper, TaskEventMapper taskEventMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.taskEventMapper = taskEventMapper;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request){
        Task task = taskMapper.toEntity(request);

        Task createdTask = taskService.create(task);

        TaskResponse response = taskMapper.toResponse(createdTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity <List<TaskResponse>> findAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(defaultValue = "CREATED_AT") SortField sortBy,
            @RequestParam(defaultValue = "DESC") SortDirection direction
    ){
        List<Task> tasks = taskService.findAll(title, status, sortBy, direction);

        List<TaskResponse> response = tasks.stream().map(taskMapper::toResponse).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable Integer id){
        Task task = taskService.findById(id).orElseThrow(()-> new TaskNotFoundException("Задача не найдена"));

        TaskResponse response = taskMapper.toResponse(task);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody TaskRequest request
    ) {
        Task task = taskService.findById(id).orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        boolean updated = taskService.update(task);

        if (!updated) {
            throw new TaskNotFoundException("Не удалось обновить задачу");
        }

        TaskResponse response = taskMapper.toResponse(task);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody TaskStatusRequest request
    ){
        boolean updated = taskService.updateStatus(id, request.getStatus());

        if (!updated) {
            throw new TaskNotFoundException("Задача не найдена");
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean deleted = taskService.delete(id);

        if (!deleted) {
            throw new TaskNotFoundException("Задача не найдена");
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<TaskEventResponse>> getHistory(
            @PathVariable Integer id
    ){
        if (!taskService.existsById(id)) {
            throw new TaskNotFoundException("Задача не найдена");
        }

        List<TaskEventResponse> response = taskService.getHistory(id).stream().map(taskEventMapper::toResponse).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
