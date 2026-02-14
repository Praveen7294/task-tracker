package com.example.taskcli.repository.model;

import com.example.taskcli.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskStorage {

    private int lastId;
    private final List<Task> tasks;

    public TaskStorage() {
        this.lastId = 0;
        this.tasks = new ArrayList<>();
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

}
