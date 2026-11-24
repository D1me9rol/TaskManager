package com.example.taskmanager.model;

import java.time.LocalDateTime;

public class TaskEvent {
    private Integer id;
    private Integer taskId;
    private TaskEventType eventType;
    private LocalDateTime eventTime;

    public TaskEvent(Integer id, Integer taskId, TaskEventType eventType, LocalDateTime eventTime) {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void setEventType(TaskEventType eventType) {
        this.eventType = eventType;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }
}
