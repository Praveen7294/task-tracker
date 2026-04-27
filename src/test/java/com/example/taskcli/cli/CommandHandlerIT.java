package com.example.taskcli.cli;

import com.example.taskcli.repository.FileTaskRepository;
import com.example.taskcli.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandHandlerIT {

    private CommandHandler commandHandler;

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        Path filePath = tempDir.resolve("tasks.json");

        FileTaskRepository taskRepository = new FileTaskRepository(filePath);
        TaskService service = new TaskService(taskRepository);

        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();

        commandHandler = new CommandHandler(service,
                new PrintStream(outContent), new PrintStream(errContent));
    }

    @Test
    public void addSingleTask() {

        commandHandler.handle(new String[] {"add", "First Task"});

        assertEquals("Task added Successfully (ID: " + 1 + ")\n",
                outContent.toString().replace("\r\n", "\n"));
    }

    @Test
    public void addMultipleTasks() {
        commandHandler.handle(new String[] {"add", "First Task"});
        commandHandler.handle(new String[] {"add", "Second Task"});

        assertEquals("""
                Task added Successfully (ID: 1)
                Task added Successfully (ID: 2)
                """, outContent.toString().replace("\r\n", "\n"));
    }

    @Test
    public void deleteTask() {
        commandHandler.handle(new String[] {"add", "First Task"});
        commandHandler.handle(new String[] {"add", "Second Task"});

        commandHandler.handle(new String[] {"delete", "1"});

        assertEquals("""
                Task added Successfully (ID: 1)
                Task added Successfully (ID: 2)
                Task deleted Successfully
                """, outContent.toString().replace("\r\n", "\n"));
    }

    @Test
    public void InvalidCommand() {

        commandHandler.handle(new String[] {"delete"});

        assertEquals("Error: Invalid command\n",
                errContent.toString().replace("\r\n", "\n"));
    }

}
