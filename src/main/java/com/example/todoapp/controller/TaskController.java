package com.example.todoapp.controller;


import com.example.todoapp.model.enums.Priority;
import com.example.todoapp.model.enums.Status;
import com.example.todoapp.model.User;
import com.example.todoapp.service.TodoTaskService;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TodoTaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping("/create") //endpoint
    public ResponseEntity<TodoTask> createTask(@RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("priority") String priority,
        @RequestParam("deadline") String deadline,
        @RequestParam("status") String status,
        @RequestParam("userId") int userId) {
        log.info("Received request with status: {} ", status);
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        LocalDate convertedDeadline = LocalDate.parse(deadline);
        log.info("Before creating TodoTask");
        TodoTask todoTask = new TodoTask(title, description, Priority.valueOf(priority), convertedDeadline,
            Status.valueOf(status));
        log.info("After creating TodoTask");
        todoTask.setUser(user);
        return ResponseEntity.ok(taskService.insertTask(todoTask));
    }

    @PostMapping("/update/{taskId}")
    public ResponseEntity<TodoTask> updateTasks(@PathVariable("taskId") Integer taskId,
        @RequestBody TodoTask task) {
        log.info("Received RequestBody: {}", task);
        // TODO add exception handling here - try catch block (TaskNotFoundException)
        TodoTask todoTask = taskService.updateTask(taskId, task);
        return ResponseEntity.ok(todoTask);
    }

    @PostMapping("/update")
    public ResponseEntity<TodoTask> updateTaskById(@RequestParam("taskId") Integer taskId,
        @RequestParam("title") String title,
        @RequestParam("priority") String priority,
        @RequestParam("deadline") String deadline) {
        log.info("Received request with title: {}, deadline: {}", title, deadline);
        LocalDate convertedDeadline = LocalDate.parse(deadline);
        TodoTask todoTask = taskService.updateTaskById(taskId, title, Priority.valueOf(priority), convertedDeadline);
        return ResponseEntity.ok(todoTask);
    }

    @GetMapping("/list") //endpoint
    public ResponseEntity<List<TodoTask>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/")
    public ResponseEntity<TodoTask> getTaskById(@RequestParam("id") Integer taskId) {
        // TODO add exception handling here - try catch block (TaskNotFoundException)
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeTask(@RequestParam("id") Integer taskId) {
        // TODO add exception handling here - try catch block (TaskNotFoundException)
        taskService.deleteTaskById(taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter/priority")
    public ResponseEntity<List<TodoTask>> getTaskByPriority(@RequestParam("priority") Priority priority) {
        // TODO add exception handling here - try catch block (TaskNotFoundException)
        return ResponseEntity.ok(taskService.getTaskByPriority(priority));
    }

    @GetMapping("/filter/deadline")
    public ResponseEntity<List<TodoTask>> getTaskByDeadline(@RequestParam("deadline") LocalDate deadline) {
        // TODO add exception handling here - try catch block (TaskNotFoundException)
        return ResponseEntity.ok(taskService.getTaskByDeadline(deadline));

    }

    @GetMapping("/filter/status")
    public ResponseEntity<List<TodoTask>> getTaskByStatus(@RequestParam("status") String userStatus) {
        // TODO add exception handling here - try catch block (TaskNotFoundException)
        Status status = taskService.getStatusFromUserStatus(userStatus);
        return ResponseEntity.ok(taskService.getTaskByStatus(status));
    }

    @GetMapping("/filter/user")
    public ResponseEntity<List<TodoTask>> getAllTasksByUserId(@RequestParam("userId") int userId) {
        // TODO add exception handling here - try catch block (UserNotFoundException)
        return ResponseEntity.ok(userService.getTasksByUserId(userId));
    }

}
