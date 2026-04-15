package com.example.taskcli.cli;

import com.example.taskcli.model.Task;
import com.example.taskcli.model.TaskStatus;
import com.example.taskcli.service.TaskService;

import java.io.PrintStream;
import java.util.List;

public class CommandHandler {

    private final TaskService taskService;
    private final PrintStream out;
    private final PrintStream err;

    public CommandHandler(TaskService taskService, PrintStream out, PrintStream err) {
        this.taskService = taskService;
        this.out = out;
        this.err = err;
    }

    public void handle(String[] args) {

        if (args.length == 0) {
            err.println("Error: No command specified");
            err.println("Usage: task-cli <command> [arguments]");
            out.println();
            err.println("For help use: task-cli help");
            return;
        }

        switch (args[0]) {
            case"show" :
                handleShow(args);
                break;
            case "add" :
                handleAdd(args);
                break;
            case "update" :
                handleUpdate(args);
                break;
            case "mark-in-progress" :
                handleMarkInProgress(args);
                break;
            case "mark-done" :
                handleMarkDone(args);
                break;
            case "list" :
                handleList(args);
                break;
            case "delete" :
                handleDelete(args);
                break;
            case "help" :
                handleHelp();
                break;
            default:
                err.println("Error: Unknown command: " + args[0]);
                out.println();
                err.println("For help use: task-cli help");
        }
    }

    private void handleHelp() {
        out.println("command list:");
        out.println();
        out.println("show: use `show` to see a task followed by task ID.");
        out.println("add: use `add` to add task followed by task message.");
        out.println("update: use `update` to update task message followed by task id and message.");
        out.println("mark-in-progress: use `mark-in-progress` to " +
                "update task status to `in-progress` followed by task ID.");
        out.println("mark-done: use `mark-done` to update task status to `done` followed by task ID.");
        out.println("list: use `list` to get all tasks.");
        out.println("list todo: use `list` and `todo` together to get all todo tasks.");
        out.println("list in-progress: use `list` and `in-progress` together to get all in-progress tasks.");
        out.println("list done: use `list` and `done` together to get all done tasks.");
        out.println("delete: use `delete` to delete a task followed by task ID.");
        out.println("help: use `help` to show this command message");
    }

    private void handleShow(String[] args) {
        if (args.length != 2) {
            err.println("Error: Invalid command");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            Task task = taskService.getTask(id);

            printTasks(List.of(task));
        } catch (NumberFormatException e) {
            err.println("Error: Task ID must be a number");
        } catch (IllegalArgumentException e) {
            err.println("Task Not found: " + args[1]);
        }
    }

    private void handleAdd(String[] args) {
        if (args.length != 2) {
            err.println("Error: Invalid command");
            return;
        }

        String description = args[1];

        Task task = taskService.addTask(description);
        out.println("Task added Successfully (ID: " + task.getId() + ")");
    }

    private void handleUpdate(String[] args) {
        if (args.length != 3) {
            err.println("Error: Invalid command");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            String description = args[2];

            Task task = taskService.updateDescription(id, description);
            out.println("Task updated Successfully (ID: " + task.getId() + ")");
        } catch (NumberFormatException e) {
            err.println("Error: Task ID must be a number");
        }
    }

    private void handleMarkInProgress(String[] args) {
        if (args.length != 2) {
            err.println("Error: Invalid command");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            taskService.markInProgress(id);
            out.println("Task marked Successfully");
        } catch (NumberFormatException e) {
            err.println("Error: Task ID must be a number");
        }

    }

    private void handleMarkDone(String[] args) {
        if (args.length != 2) {
            err.println("Error: Invalid command");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            taskService.markDone(id);
            out.println("Task marked Successfully");
        } catch (NumberFormatException e) {
            err.println("Error: Task ID must be a number");
        }
    }

    private void handleDelete(String[] args) {
        if (args.length != 2) {
            err.println("Error: Invalid command");
            return;
        }

        try {
            int id = Integer.parseInt(args[1]);
            boolean deleted = taskService.deleteTask(id);

            if (deleted) {
                out.println("Task deleted Successfully");
            } else {
                out.println("Task deletion Failed");
            }
        } catch (NumberFormatException e) {
            err.println("Error: Task ID must be a number");
        }
    }

    private void handleList(String[] args) {
        if (args.length > 2) {
            err.println("Error: Invalid command");
            return;
        }

        List<Task> tasks;
        if (args.length == 1) {
            tasks = taskService.getAllTasks();
            printTasks(tasks);
            return;
        }

        switch (args[1]) {
            case "todo" :
                tasks = taskService.getTasksByStatus(TaskStatus.TODO);
                break;
            case "in-progress" :
                tasks = taskService.getTasksByStatus(TaskStatus.IN_PROGRESS);
                break;
            case "done" :
                tasks = taskService.getTasksByStatus(TaskStatus.DONE);
                break;
            default:
                err.println("Error: Unknown status: " + args[1]);
                out.println();
                err.println("For help use: task-cli help");
                return;
        }

        printTasks(tasks);
    }

    private void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            out.println("No tasks found");
        }

        for (Task task : tasks) {
            out.println();
            out.println("Id: " + task.getId());
            out.println("Description: " + task.getDescription());
            out.println("Status: " + task.getStatus());
            out.println("Last Updated: " + task.getUpdatedAt());
            out.println("Created On: " + task.getCreatedAt());
            out.println();
            out.println("--------------------------------------------------------");
        }
    }
}
