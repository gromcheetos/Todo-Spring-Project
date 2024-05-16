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
        User user = userService.createUser(userName, userEmail);
        return ResponseEntity.ok(user);
    }

    /*@PostMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Integer userId,
                                           @RequestBody User patch){
        User updatedUser = userService.updateUser(userId, patch);
        return ResponseEntity.ok(updatedUser);
    }*/

    @PostMapping("/update")
    public ResponseEntity<User> updateUserAlternative(@RequestParam("userId") Integer userId,
                                                      @RequestParam("name") String name,
                                                      @RequestParam("email") String email){
        try{
            User updatedUser = userService.updateUserById(userId, name, email);
            return ResponseEntity.ok(updatedUser);
        }catch(UserNotFoundException exception){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Integer userId){
        try{
            userService.deleteUserById(userId);
            return ResponseEntity.ok().build();
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
