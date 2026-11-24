package com.example.taskmanager.repository;

import com.example.taskmanager.model.SortDirection;
import com.example.taskmanager.model.SortField;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.TaskStatus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Task> findAll(
            String title,
            TaskStatus status,
            SortField sortBy,
            SortDirection direction
    ) {
        String sql = """
            SELECT id, title, description, status, created_at, deleted
            FROM tasks
            WHERE deleted = FALSE
              AND (? IS NULL OR LOWER(title) LIKE LOWER(?))
              AND (? IS NULL OR status = ?)
            ORDER BY
                CASE WHEN ? = 'title' AND ? = 'ASC' THEN title END ASC,
                CASE WHEN ? = 'title' AND ? = 'DESC' THEN title END DESC,
                CASE WHEN ? <> 'title' AND ? = 'ASC' THEN created_at END ASC,
                CASE WHEN ? <> 'title' AND ? = 'DESC' THEN created_at END DESC
            """;

        String searchTitle = (title == null || title.isBlank()) ? null : "%" + title.trim() + "%";

        String sortField = sortBy == SortField.TITLE ? "title" : "created_at";
        String sortDirection = direction.name();

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        TaskStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getBoolean("deleted")
                ),
                searchTitle, searchTitle,
                status == null ? null : status.name(),
                status == null ? null : status.name(),
                sortField, sortDirection,
                sortField, sortDirection,
                sortField, sortDirection,
                sortField, sortDirection
        );
    }

    public Task create(Task task){
        String sql = """
            INSERT INTO tasks (title, description, status, created_at, deleted)
            VALUES (?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getStatus().name());
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setBoolean(5, false);

            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();

        if (generatedId == null) {
            throw new IllegalStateException("Не удалось получить ID созданной задачи");
        }

        task.setId(generatedId.intValue());
        task.setCreatedAt(now);
        task.setDeleted(false);

        return task;
    }

    public Optional<Task> findById(Integer id){
        String sql = """
            SELECT id, title, description, status, created_at, deleted
            FROM tasks
            WHERE id = ? AND deleted = FALSE
            """;

        List<Task> tasks = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        TaskStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getBoolean("deleted")
                        ), id);

        return tasks.stream().findFirst();
    }

    public boolean existsById(Integer id) {
        String sql = """
            SELECT COUNT(*)
            FROM tasks
            WHERE id = ?
            """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);

        return count != null && count > 0;
    }

    public boolean update(Task task) {
        String sql = """
            UPDATE tasks
            SET title = ?, description = ?, status = ?
            WHERE id = ? AND deleted = FALSE
            """;

        int rowsAffected = jdbcTemplate.update(
                sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getId()
        );

        return rowsAffected > 0;
    }

    public boolean updateStatus(Integer id, TaskStatus status) {
        String sql = """
            UPDATE tasks
            SET status = ?
            WHERE id = ? AND deleted = FALSE
            """;

        int rowsAffected = jdbcTemplate.update(sql, status.name(), id);

        return rowsAffected > 0;
    }

    public boolean delete(Integer id) {
        String sql = """
            UPDATE tasks
            SET deleted = TRUE
            WHERE id = ? AND deleted = FALSE
            """;

        int rowsAffected = jdbcTemplate.update(sql, id);

        return rowsAffected > 0;
    }
}
