package com.example.taskcli.repository.model;

import com.example.taskcli.model.Task;

import java.util.List;

public class TaskStorage {

    private int lastId;
    private List<Task> tasks;

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
