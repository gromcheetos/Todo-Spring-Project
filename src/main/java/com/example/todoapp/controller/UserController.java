package com.example.todoapp.controller;

import com.example.todoapp.exceptions.UserNotFoundException;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.model.User;
import com.example.todoapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(
                                           @RequestParam("userName") String userName,
                                           @RequestParam("userEmail") String userEmail){
        User user = new User(userName, userEmail);
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam("userId") Integer userId,
                                                      @RequestParam("userName") String name,
                                                      @RequestParam("userEmail") String email){
        try{
            User updatedUser = userService.updateUserById(userId, name, email);
            return ResponseEntity.ok(updatedUser);
        }catch(UserNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Integer userId){
        try{
            userService.deleteUserById(userId);
            return ResponseEntity.noContent().build();
        }catch(UserNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping ("/list")
    public ResponseEntity<List<User>> getAllUsers(){
            return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/search")
    public ResponseEntity<User> getUserById(@RequestParam("userId") Integer userId){
        try{
            return ResponseEntity.ok(userService.getUserById(userId));
        }catch(UserNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
