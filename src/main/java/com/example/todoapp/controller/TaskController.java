package com.example.todoapp.controller;


import com.example.todoapp.exceptions.TaskNotFoundException;
import com.example.todoapp.exceptions.UserNotFoundException;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
        @RequestParam("deadline") LocalDate deadline,
        @RequestParam("status") String status,
        @RequestParam("userId") int userId) throws UserNotFoundException {
        log.info("Received request with status: {} ", status);
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
       // LocalDate convertedDeadline = LocalDate.parse(deadline);
        log.info("Before creating TodoTask");
        TodoTask todoTask = new TodoTask(title, description, Priority.valueOf(priority), deadline,
            Status.valueOf(status));
        log.info("After creating TodoTask");
        todoTask.setUser(user);
        return ResponseEntity.ok(taskService.insertTask(todoTask));
    }

    @PostMapping("/update/{taskId}")
    public ResponseEntity<TodoTask> updateTasks(@PathVariable("taskId") Integer taskId,
        @RequestBody TodoTask task) {
        log.info("Received RequestBody: {}", task);
        try{
            TodoTask todoTask = taskService.updateTask(taskId, task);
            return ResponseEntity.ok(todoTask);
        }catch(TaskNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list") //endpoint
    public ResponseEntity<List<TodoTask>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/")
    public ResponseEntity<TodoTask> getTaskById(@RequestParam("id") Integer taskId) {
        try{
            return ResponseEntity.ok(taskService.getTaskById(taskId));
        }catch(TaskNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeTask(@RequestParam("id") Integer taskId) {
        try{
            taskService.deleteTaskById(taskId);
            return ResponseEntity.noContent().build();
        }catch(TaskNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter/priority")
    public ResponseEntity<List<TodoTask>> getTaskByPriority(@RequestParam("priority") Priority priority) {
        try{
            return ResponseEntity.ok(taskService.getTaskByPriority(priority));
        }catch(TaskNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter/deadline")
    public ResponseEntity<List<TodoTask>> getTaskByDeadline(@RequestParam("deadline") LocalDate deadline) {
        try{
            return ResponseEntity.ok(taskService.getTaskByDeadline(deadline));
        }catch(TaskNotFoundException exception){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter/status")
    public ResponseEntity<List<TodoTask>> getTaskByStatus(@RequestParam("status") String status) {
        try{
            return ResponseEntity.ok(taskService.getTaskByStatus(Status.valueOf(status)));
        }catch(TaskNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/filter/user")
    public ResponseEntity<List<TodoTask>> getAllTasksByUserId(@RequestParam("userId") int userId) {
        try{
            return ResponseEntity.ok(userService.getTasksByUserId(userId));
        }catch(UserNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
