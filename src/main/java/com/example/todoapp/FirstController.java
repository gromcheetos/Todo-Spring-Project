package com.example.todoapp;


import com.example.todoapp.model.TodoTask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @PostMapping("/create/task") //endpoint
    public ResponseEntity<TodoTask> helloWorld(@RequestParam("title") String title,
                                               @RequestParam("description") String description) {
        TodoTask todoTask = new TodoTask(title, description);
        return ResponseEntity.ok(todoTask);
    }
}