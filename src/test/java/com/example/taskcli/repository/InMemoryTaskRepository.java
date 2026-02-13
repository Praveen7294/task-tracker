package com.example.taskcli.repository;

import com.example.taskcli.model.Task;
import com.example.taskcli.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {

    private final Map<Integer, Task> taskMap = new HashMap<>();
    private int lastId = 0;

    @Override
    public Task save(Task task) {
        taskMap.put(task.getId(), task);
        lastId = task.getId();
        return task;
    }

    @Override
    public Optional<Task> findById(int id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return taskMap.values().stream()
                .filter(t -> t.getStatus() == status).toList();
    }

    @Override
    public boolean deleteById(int id) {
        return taskMap.remove(id) != null;
    }

    @Override
    public int getNextId() {
        return ++lastId;
    }
}
