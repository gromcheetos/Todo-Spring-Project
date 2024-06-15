package com.example.todoapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomePageController {

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: " + auth.toString());
        if (auth != null) {
            System.out.println("Authentication: " + auth);
            if (auth.isAuthenticated() && !(auth.getPrincipal() instanceof String && auth.getPrincipal().equals("anonymousUser"))) {
                log.info("User is authenticated. Redirecting to /list-test");
                return "redirect:/task/list";
            }
        } else {
            log.info("Authentication is null");
        }
        return "home";
    }

}
