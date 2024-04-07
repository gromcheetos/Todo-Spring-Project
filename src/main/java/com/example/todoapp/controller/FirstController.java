package com.example.todoapp.controller;


import com.example.todoapp.service.TodoTaskService;
import com.example.todoapp.model.TodoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FirstController {

    @Autowired
    private TodoTaskService taskService;

    @PostMapping("/create/task") //endpoint
    public ResponseEntity<TodoTask> createTask(@RequestParam("title") String title,
                                               @RequestParam("description") String description) {
        TodoTask todoTask = new TodoTask(title, description);
        taskService.saveOrUpdateTask(todoTask);
        return ResponseEntity.ok(todoTask);
    }
    @GetMapping("/tasks/list") //endpoint
    public ResponseEntity<List<TodoTask>> getAllTasks(){
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    @GetMapping("/tasks")
    public ResponseEntity<TodoTask> getTaskById(@RequestParam("id") Integer taskId){
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }
    @PostMapping("/tasks/remove")
    public ResponseEntity<?> removeTask(@RequestParam("id") Integer taskId){
        taskService.deleteTaskById(taskId);
        return ResponseEntity.ok().build();
    }
}