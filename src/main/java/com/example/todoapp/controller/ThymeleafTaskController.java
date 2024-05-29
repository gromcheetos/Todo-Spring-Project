package com.example.todoapp.controller;

import com.example.todoapp.exceptions.TaskNotFoundException;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.service.TodoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ThymeleafTaskController {

    @Autowired
    private TodoTaskService taskService;

    @RequestMapping("/list-test")
    public String getAllTasks(Model model) {
        model.addAttribute("listOfTasks", taskService.getAllTasks());
        return "index";
    }

    @GetMapping
    public String getNumberOfItem(Model model) {
        long totalNumberOfItems = taskService.getCount();
        if(totalNumberOfItems > 0) {
            model.addAttribute("totalNumberOfItems", totalNumberOfItems);
        }
        return "index";
    }
    @GetMapping("/detail/{taskId}")
    public String getTaskDetail(@PathVariable("taskId") int id, Model model) throws TaskNotFoundException {
        TodoTask task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "task-detail";
    }
}
