package com.example.taskcli.repository;

import com.example.taskcli.model.Task;
import com.example.taskcli.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(int id);

    List<Task> findAll();

    List<Task> findByStatus(TaskStatus status);

    void deleteById(int id);

    int getNextId();
}
