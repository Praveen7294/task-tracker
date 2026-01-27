package com.example.taskcli.repository;

import com.example.taskcli.model.Task;
import com.example.taskcli.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public class FileTaskRepository implements TaskRepository {

    @Override
    public Task save(Task task) {
        return task;
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        return List.of();
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return List.of();
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public int getNextId() {
        return 1;
    }
}
