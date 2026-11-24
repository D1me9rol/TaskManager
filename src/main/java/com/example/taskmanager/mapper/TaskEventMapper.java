package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.TaskEventResponse;
import com.example.taskmanager.model.TaskEvent;
import org.springframework.stereotype.Component;

@Component
public class TaskEventMapper {
    public TaskEventResponse toResponse(TaskEvent event){
        return new TaskEventResponse(
                event.getId(),
                event.getTaskId(),
                event.getEventType(),
                event.getEventTime()
        );
    }
}
