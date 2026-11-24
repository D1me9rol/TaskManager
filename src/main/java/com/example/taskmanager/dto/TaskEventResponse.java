package com.example.taskmanager.dto;

import com.example.taskmanager.model.TaskEventType;

import java.time.LocalDateTime;

public class TaskEventResponse {
    private final Integer id;
    private final Integer taskId;
    private final TaskEventType eventType;
    private final LocalDateTime eventTime;

    public TaskEventResponse(Integer id, Integer taskId, TaskEventType eventType, LocalDateTime eventTime) {
        this.id = id;
        this.taskId = taskId;
        this.eventType = eventType;
        this.eventTime = eventTime;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public TaskEventType getEventType() {
        return eventType;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }
}
