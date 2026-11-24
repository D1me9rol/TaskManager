package com.example.taskmanager.dto;

import com.example.taskmanager.model.TaskStatus;

import java.time.LocalDateTime;

public class TaskResponse {
    private final Integer id;
    private final String title;
    private final String description;
    private final TaskStatus status;
    private final LocalDateTime createdAt;

    public TaskResponse(Integer id, String title, String description, TaskStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
