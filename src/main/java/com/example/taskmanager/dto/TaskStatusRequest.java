package com.example.taskmanager.dto;


import com.example.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class TaskStatusRequest {
    @NotNull(message="Должен быть указан статус задачи")
    private TaskStatus status;

    public TaskStatusRequest() {
    }

    public TaskStatusRequest(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus (TaskStatus status) {
        this.status = status;
    }
}
