package com.example.todoapp.controller;

import com.example.todoapp.model.TodoTask;
import com.example.todoapp.model.User;
import com.example.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// TODO: please add a base path "users"
// TODO: and adjust the remaining paths accordingly
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create/user")
    public ResponseEntity<User> createUser(
                                           @RequestParam("userName") String userName,
                                           @RequestParam("userEmail") String userEmail){
        User user = userService.createUser(userName, userEmail);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/update/user/{userId}")// ?? how
    public ResponseEntity<User> updateUser(@PathVariable("userId") Integer userId,
                                           @RequestBody User patch){
        User updatedUser = userService.updateUser(userId, patch);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/update/user")
    public ResponseEntity<User> updateUserAlternative(@RequestParam("userId") Integer userId,
                                                      @RequestParam("name") String name,
                                                      @RequestParam("email") String email){
        User updatedUser = userService.updateUserById(userId, name, email);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/delete/user")
    public ResponseEntity<?> deleteUser(@RequestParam("userId") Integer userId){
        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping ("/user/list")
    public ResponseEntity<List<User>> getAllUsers(){
            return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/user/search")
    public ResponseEntity<User> getUserById(@RequestParam("userId") Integer userId){
        return ResponseEntity.ok(userService.getUserById(userId));

    }
}
