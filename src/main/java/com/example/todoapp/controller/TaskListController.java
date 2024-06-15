package com.example.todoapp.controller;

import com.example.todoapp.exceptions.TaskNotFoundException;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.service.TodoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class TaskListController {


    @Autowired
    private TodoTaskService taskService;

    @GetMapping("/task/list")
    public String getAllTasks(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: " + auth.toString());
        model.addAttribute("listOfTasks", taskService.getAllTasks());
        return "index";
    }

    @GetMapping("/tasks/count")
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
