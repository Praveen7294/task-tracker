package com.example.taskcli.service;

import com.example.taskcli.model.Task;
import com.example.taskcli.model.TaskStatus;
import com.example.taskcli.repository.InMemoryTaskRepository;
import com.example.taskcli.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        TaskRepository taskRepository = new InMemoryTaskRepository();
        taskService = new TaskService(taskRepository);
    }

    @Test
    public void testAddTask() {
        Task task = taskService.addTask("Buy groceries");

        assertEquals(1, task.getId());
        assertEquals("Buy groceries", task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getUpdatedAt());
    }

    @Test
    public void testUpdateDescription() {
        Task createdTask = taskService.addTask("Buy groceries");
        Task updatedTask =
                taskService.updateDescription(createdTask.getId(), "Buy groceries and cook");

        assertEquals("Buy groceries and cook", updatedTask.getDescription());
    }

    @Test
    public void testMarkInProgress() {
        Task createdTask = taskService.addTask("Buy groceries");
        taskService.markInProgress(createdTask.getId());

        Task updatedTask = taskService.getTask(createdTask.getId());
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus());
    }

    @Test
    public void testMarkDone() {
        Task createdTask = taskService.addTask("Buy groceries");
        taskService.markDone(createdTask.getId());

        Task updatedTask = taskService.getTask(createdTask.getId());
        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
    }

    @Test
    public void testDeleteTask() {
        Task createdTask = taskService.addTask("Buy groceries");

        boolean deleted = taskService.deleteTask(createdTask.getId());

        assertTrue(deleted);
        assertThrows(IllegalArgumentException.class, () -> taskService.getTask(createdTask.getId()));
    }

    @Test
    public void testGetTask() {
        Task createdTask = taskService.addTask("Buy groceries");
        Task savedTask = taskService.getTask(createdTask.getId());

        assertEquals(createdTask.getId(), savedTask.getId());
        assertEquals(createdTask.getDescription(), savedTask.getDescription());
        assertEquals(createdTask.getDescription(), savedTask.getDescription());
        assertEquals(createdTask.getCreatedAt(), savedTask.getCreatedAt());
        assertEquals(createdTask.getUpdatedAt(), savedTask.getUpdatedAt());
    }

    @Test
    public void testGetAllTasks() {
        taskService.addTask("Buy groceries");
        taskService.addTask("Study");
        taskService.addTask("Office work");

        List<Task> allTasks = taskService.getAllTasks();

        assertEquals(3, allTasks.size());
    }

    @Test
    public void testGetTasksByStatus() {
        taskService.addTask("Buy groceries");
        Task task1 = taskService.addTask("Study");
        taskService.markInProgress(task1.getId());
        Task task2 = taskService.addTask("Office work");
        taskService.markDone(task2.getId());

        List<Task> todoTask = taskService.getTasksByStatus(TaskStatus.TODO);
        List<Task> inProgressTask = taskService.getTasksByStatus(TaskStatus.IN_PROGRESS);
        List<Task> doneTask = taskService.getTasksByStatus(TaskStatus.DONE);

        assertEquals(1, todoTask.size());
        assertEquals(1, inProgressTask.size());
        assertEquals(1, doneTask.size());
    }
}
