package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskRequest {
    @NotBlank(message = "Название задачи не может быть пустым")
    @Size(max=255, message = "Название задачи не должно быть длиннее 255 символов")
    private String title;

    @Size(max=2000, message = "Описание задачи не должно быть длиннее 2000 символов")
    private String description;

    public TaskRequest() {
    }

    public TaskRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
