package com.example.taskcli;

import com.example.taskcli.cli.CommandHandler;
import com.example.taskcli.repository.FileTaskRepository;
import com.example.taskcli.service.TaskService;

import java.nio.file.Path;

public class TaskTrackerApplication {

    public static void main(String[] args) {

        Path filePath = Path.of("data", "tasks.json");

        FileTaskRepository taskRepository = new FileTaskRepository(filePath);
        TaskService service = new TaskService(taskRepository);

        CommandHandler commandHandler = new CommandHandler(service, System.out, System.err);

        commandHandler.handle(args);
    }
}
