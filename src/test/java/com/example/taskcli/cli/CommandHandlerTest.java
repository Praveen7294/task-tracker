package com.example.taskcli.cli;

import com.example.taskcli.model.Task;
import com.example.taskcli.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommandHandlerTest {

    private TaskService taskService;
    private CommandHandler commandHandler;

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);

        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();

        commandHandler = new CommandHandler(
                taskService,
                new PrintStream(outContent),
                new PrintStream(errContent)
        );
    }

    @Test
    public void shouldPrintErrorWhenNoArguments() {
        commandHandler.handle(new String[] {});

        String errorMsg = errContent.toString().replace("\r\n", "\n");
        assertEquals("""
                        Error: No command specified
                        Usage: task-cli <command> [arguments]
                        """, errorMsg);
        assertEquals("\nFor help use: task-cli help\n",
                outContent.toString().replace("\r\n", "\n"));
    }

    @Test
    public void shouldHandleInvalidId() {
        commandHandler.handle(new String[] {"delete", "abc"});

        assertEquals("Error: Task ID must be a number\n",
                errContent.toString().replace("\r\n", "\n"));

        verify(taskService, never()).deleteTask(anyInt());
    }

    @Test
    public void shouldDeleteTaskSuccessfully() {
        when(taskService.deleteTask(1)).thenReturn(true);

        commandHandler.handle(new String[] {"delete", "1"});

        assertEquals("Task deleted Successfully\n",
                outContent.toString().replace("\r\n", "\n"));

        verify(taskService).deleteTask(1);
    }

    @Test
    public void shouldAddTaskSuccessfully() {
        Task task = new Task(1, "Testing", LocalDateTime.now());

        when(taskService.addTask("Testing")).thenReturn(task);

        commandHandler.handle(new String[] {"add", "Testing"});

        assertEquals("Task added Successfully (ID: " + 1 + ")\n",
                outContent.toString().replace("\r\n", "\n"));

        verify(taskService).addTask("Testing");
    }

    @Test
    public void shouldHandleTaskNotFound() {

        when(taskService.getTask(1))
                .thenThrow(new IllegalArgumentException(""));

        commandHandler.handle(new String[] {"show", "1"});

        assertEquals("Task Not found: " + "1\n",
                errContent.toString().replace("\r\n", "\n"));
    }
}
