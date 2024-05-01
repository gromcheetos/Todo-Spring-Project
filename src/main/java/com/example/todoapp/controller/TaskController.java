package com.example.todoapp.controller;


import com.example.todoapp.model.Priority;
import com.example.todoapp.model.Status;
import com.example.todoapp.model.User;
import com.example.todoapp.service.TodoTaskService;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TodoTaskService taskService;
    @Autowired
    private UserService userService;

    @PostMapping("/create/task") //endpoint
    public ResponseEntity<TodoTask> createTask(@RequestParam("title") String title,
                                               @RequestParam("description") String description,
                                               @RequestParam("priority") String priority,
                                               @RequestParam("deadline")String deadline,
                                               @RequestParam("status") String status,
                                               @RequestParam("userId") int userId) {
        System.out.println("Received request with status: \"" + status + "\"");
        User user = userService.getUserById(userId);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        LocalDate convertedDeadline = LocalDate.parse(deadline);
        System.out.println("Before creating TodoTask");
        TodoTask todoTask = new TodoTask(title, description, Priority.valueOf(priority), convertedDeadline, Status.valueOf(status));
        System.out.println("After creating TodoTask");
        todoTask.setUser(user);
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
    @GetMapping("/tasks/filter/priority")
    public ResponseEntity<List<TodoTask>> getTaskByPriority(@RequestParam("priority") Priority priority){
        return ResponseEntity.ok(taskService.getTaskByPriority(priority));
    }
    @GetMapping("/tasks/filter/deadline")
    public ResponseEntity<List<TodoTask>> getTaskByDeadline(@RequestParam("deadline") LocalDate deadline){
        return ResponseEntity.ok(taskService.getTaskByDeadline(deadline));

    }
    @GetMapping("/tasks/filter/status")
    public ResponseEntity<List<TodoTask>> getTaskByStatus(@RequestParam("status") String userStatus){
        Status status = taskService.getStatusFromUserStatus(userStatus);
        return ResponseEntity.ok(taskService.getTaskByStatus(status));
        }
    @GetMapping ("/tasks/filter/user")
    public ResponseEntity<List<TodoTask>> getAllTasksByUserId(@RequestParam("userId") int userId){
        return ResponseEntity.ok(userService.getTasksByUserId(userId));
        }


    }
