package com.example.taskmanager.service;
import com.example.taskmanager.model.*;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.TaskEventRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskEventRepository taskEventRepository;

    public TaskService(TaskRepository taskRepository, TaskEventRepository taskEventRepository) {
        this.taskRepository = taskRepository;
        this.taskEventRepository = taskEventRepository;
    }

    @Transactional
    public Task create(Task task){
        task.setStatus(TaskStatus.NEW);

        Task createdTask = taskRepository.create(task);

        TaskEvent event = new TaskEvent(null, createdTask.getId(), TaskEventType.CREATED, LocalDateTime.now());

        taskEventRepository.save(event);

        return createdTask;
    }

    public List<Task> findAll(
            String title,
            TaskStatus status,
            SortField sortBy,
            SortDirection direction
    ) {
        return taskRepository.findAll(title, status, sortBy, direction);
    }

    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    public boolean existsById(Integer id) {
        return taskRepository.existsById(id);
    }

    @Transactional
    public boolean update(Task task) {
        boolean updated = taskRepository.update(task);

        if (!updated) {
            return false;
        }

        TaskEvent event = new TaskEvent(null, task.getId(), TaskEventType.UPDATED, LocalDateTime.now());

        taskEventRepository.save(event);

        return true;
    }

    @Transactional
    public boolean updateStatus(Integer id, TaskStatus status) {
        boolean updated = taskRepository.updateStatus(id, status);

        if (!updated) {
            return false;
        }

        TaskEvent event = new TaskEvent(null, id, TaskEventType.UPDATED, LocalDateTime.now());

        taskEventRepository.save(event);

        return true;
    }

    @Transactional
    public boolean delete(Integer id) {
        boolean deleted = taskRepository.delete(id);

        if (!deleted) {
            return false;
        }

        TaskEvent event = new TaskEvent(null, id, TaskEventType.DELETED, LocalDateTime.now());

        taskEventRepository.save(event);

        return true;
    }

    public List<TaskEvent> getHistory(Integer taskId) {
        return taskEventRepository.findByTaskId(taskId);
    }
}
