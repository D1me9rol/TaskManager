package com.example.taskmanager.repository;

import com.example.taskmanager.model.TaskEvent;
import com.example.taskmanager.model.TaskEventType;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class TaskEventRepository {
    private final JdbcTemplate jdbcTemplate;

    public TaskEventRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(TaskEvent event) {
        String sql = """
            INSERT INTO task_events (task_id, event_type, event_time)
            VALUES (?, ?, ?)
            """;

        jdbcTemplate.update(
                sql,
                event.getTaskId(),
                event.getEventType().name(),
                Timestamp.valueOf(event.getEventTime())
        );
    }

    public List<TaskEvent> findByTaskId(Integer taskId) {
        String sql = """
            SELECT id, task_id, event_type, event_time
            FROM task_events
            WHERE task_id = ?
            ORDER BY event_time DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new TaskEvent(
                rs.getInt("id"),
                rs.getInt("task_id"),
                TaskEventType.valueOf(rs.getString("event_type")),
                rs.getTimestamp("event_time").toLocalDateTime()
                ), taskId);
    }

}
