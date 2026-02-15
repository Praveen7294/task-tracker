package com.example.taskcli.repository;

import com.example.taskcli.model.Task;
import com.example.taskcli.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileTaskRepositoryIT {

    private FileTaskRepository fileTaskRepository;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        Path jsonFilePath = tempDir.resolve("tasks.json");
        fileTaskRepository = new FileTaskRepository(jsonFilePath);
    }

    @Test
    public void testSaveAndLoadTasks() {
        Task task = new Task(
                1,
                "Learn integration testing",
                LocalDateTime.now()
        );

        fileTaskRepository.save(task);

        List<Task> loadedTask = fileTaskRepository.findAll();

        assertEquals(1, loadedTask.size());
        assertEquals("Learn integration testing", loadedTask.getFirst().getDescription());
    }

    @Test
    public void testFindTaskById() {
        Task task = new Task(
                1,
                "Learn integration testing",
                LocalDateTime.now()
        );

        fileTaskRepository.save(task);

        Optional<Task> foundTask = fileTaskRepository.findById(task.getId());

        assertTrue(foundTask.isPresent());
        assertEquals("Learn integration testing", foundTask.get().getDescription());
        assertEquals(TaskStatus.TODO, foundTask.get().getStatus());
    }

    @Test
    public void testFindAllTasks() {
        Task task1 = new Task(
                1,
                "Learn integration testing",
                LocalDateTime.now()
        );
        Task task2 = new Task(
                2,
                "Buy Groceries",
                LocalDateTime.now()
        );
        Task task3 = new Task(
                3,
                "Study",
                LocalDateTime.now()
        );

        fileTaskRepository.save(task1);
        fileTaskRepository.save(task2);
        fileTaskRepository.save(task3);

        List<Task> tasks = fileTaskRepository.findAll();

        assertEquals(3, tasks.size());
        assertEquals(1, tasks.getFirst().getId());
        assertEquals(2, tasks.get(1).getId());
        assertEquals(3, tasks.get(2).getId());
    }

    @Test
    public void testFindTaskByStatus() {
        Task task1 = new Task(
                1,
                "Learn integration testing",
                LocalDateTime.now()
        );
        Task task2 = new Task(
                2,
                "Buy Groceries",
                LocalDateTime.now()
        );
        Task task3 = new Task(
                3,
                "Study",
                LocalDateTime.now()
        );

        fileTaskRepository.save(task1);
        fileTaskRepository.save(task2);
        fileTaskRepository.save(task3);

        task2 = fileTaskRepository.findById(task2.getId()).orElseThrow();
        task2.setStatus(TaskStatus.IN_PROGRESS, LocalDateTime.now());
        fileTaskRepository.save(task2);

        task3 = fileTaskRepository.findById(task3.getId()).orElseThrow();
        task3.setStatus(TaskStatus.DONE, LocalDateTime.now());
        fileTaskRepository.save(task3);

        List<Task> todoTasks = fileTaskRepository.findByStatus(TaskStatus.TODO);
        List<Task> inProgressTasks = fileTaskRepository.findByStatus(TaskStatus.IN_PROGRESS);
        List<Task> doneTasks = fileTaskRepository.findByStatus(TaskStatus.DONE);

        assertEquals(1, todoTasks.size());
        assertEquals(1, inProgressTasks.size());
        assertEquals(1, doneTasks.size());
    }

    @Test
    public void testDeleteTask() {
        Task task1 = new Task(
                1,
                "Learn integration testing",
                LocalDateTime.now()
        );
        Task task2 = new Task(
                2,
                "Buy Groceries",
                LocalDateTime.now()
        );

        fileTaskRepository.save(task1);
        fileTaskRepository.save(task2);

        fileTaskRepository.deleteById(task2.getId());
        List<Task> tasks = fileTaskRepository.findAll();
        assertEquals(1, tasks.size());

        fileTaskRepository.deleteById(task1.getId());
        tasks = fileTaskRepository.findAll();
        assertTrue(tasks.isEmpty());
    }
}
