package com.example.taskcli.service;

import com.example.taskcli.model.Task;
import com.example.taskcli.model.TaskStatus;
import com.example.taskcli.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task addTask(String description) {
        int id = taskRepository.getNextId();
        Task task = new Task(id, description, LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task updateDescription(int id, String description) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        task.setDescription(description, LocalDateTime.now());
        return taskRepository.save(task);
    }

    public void markInProgress(int id) {
        updateStatus(id, TaskStatus.IN_PROGRESS);
    }

    public void markDone(int id) {
        updateStatus(id, TaskStatus.DONE);
    }

    public void updateStatus(int id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));

        task.setStatus(status, LocalDateTime.now());
        taskRepository.save(task);
    }

    public boolean deleteTask(int id) {
        return taskRepository.deleteById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTask(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
}
