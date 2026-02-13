package com.example.taskcli.repository;

import com.example.taskcli.model.Task;
import com.example.taskcli.model.TaskStatus;
import com.example.taskcli.repository.model.TaskStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class FileTaskRepository implements TaskRepository {

    private static final Path FILE_PATH =
            Paths.get(System.getProperty("user.dir"), "data", "tasks.json");

    private final ObjectMapper objectMapper;

    public FileTaskRepository() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Task save(Task task) {
        TaskStorage taskStorage = loadFromFile();
        List<Task> tasks = taskStorage.getTasks();

        boolean exist = false;

        for(int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                exist = true;
                break;
            }
        }

        if(!exist) {
            tasks.add(task);
            taskStorage.setLastId(task.getId());
        }

        saveToFile(taskStorage);
        return task;
    }

    @Override
    public Optional<Task> findById(int id) {
        TaskStorage taskStorage = loadFromFile();

        return taskStorage.getTasks().stream().filter(t -> t.getId() == id).findFirst();
    }

    @Override
    public List<Task> findAll() {
        TaskStorage taskStorage = loadFromFile();
        return List.copyOf(taskStorage.getTasks());
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        TaskStorage taskStorage = loadFromFile();

        return taskStorage.getTasks().stream().filter(t -> t.getStatus() == status).toList();
    }

    @Override
    public void deleteById(int id) {
        TaskStorage taskStorage = loadFromFile();

        boolean removed = taskStorage.getTasks().removeIf(t -> t.getId() == id);

        if (removed) {
            saveToFile(taskStorage);
        }
    }

    @Override
    public int getNextId() {
        return loadFromFile().getLastId() + 1;
    }

    private TaskStorage loadFromFile() {
        try {
            return objectMapper.readValue(FILE_PATH.toFile(), TaskStorage.class);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read Task file", e);
        }
    }

    private void saveToFile(TaskStorage taskStorage) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(FILE_PATH.toFile(), taskStorage);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write tasks file", e);
        }
    }
}
