package com.example.taskcli.repository;

import com.example.taskcli.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
