package com.example.taskcli.model;

import java.time.LocalDateTime;

public class Task {
    private final int id;
    private String description;
    private TaskStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(int id, String description, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    public int getId() {
        return id;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setDescription(String description, LocalDateTime updatedAt) {
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public void setStatus(TaskStatus status, LocalDateTime updatedAt) {
        this.status = status;
        this.updatedAt = updatedAt;
    }
}
