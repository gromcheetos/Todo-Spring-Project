package com.example.todoapp.controller;

import com.example.todoapp.exceptions.UserNotFoundException;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.model.User;
import com.example.todoapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
        @RequestParam("name") String name,
        @RequestParam("userEmail") String userEmail,
        @RequestParam("username") String username,
        @RequestParam("password") String password) {
        log.info("Request received to register the user");
        User newUser = new User(name, userEmail, username);
        User createdUser = userService.createUser(newUser, password);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", createdUser.getId());
        return ResponseEntity.ok(responseBody);    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>>  basicLogin(@RequestParam("username") String username,
                                             @RequestParam("password") String password) {
        log.info("Request received for /users/login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        log.info("Authentication " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        log.info("User details: " + user);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("userId", user.getId());
        responseBody.put("message", "User " + user.getUsername() + " logged in successfully");
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(
        @RequestParam("userId") Integer userId,
        @RequestParam("name") String name,
        @RequestParam("userEmail") String email,
        @RequestParam("username") String username,
        @RequestParam("password") String password) {
        try {
            User updatedUser = userService.updateUserById(userId, name, email, username, password);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Integer userId) {
        try {
            User userToBeDeleted = userService.getUserById(userId);
            userService.deleteUserById(userId);
            return ResponseEntity.ok("Deleted user:" + userToBeDeleted);
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<User> getUserById(@RequestParam("userId") Integer userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (UserNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
