package com.example.todoapp.controller;

import com.example.todoapp.model.TodoTask;
import com.example.todoapp.service.TodoTaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThymeleafTestController {

    @Autowired
    private TodoTaskService taskService;

    @RequestMapping("/list-test")
    public String getAllTasks(Model model) {
        model.addAttribute("listOfTasks", taskService.getAllTasks());
        return "index";
    }

}
