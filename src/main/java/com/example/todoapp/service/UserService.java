package com.example.todoapp.service;

import com.example.todoapp.model.TodoTask;
import com.example.todoapp.model.User;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;


    public User createUser(String name, String email){
        return userRepository.save(new User(name, email));
    }
    public User updateUser(Integer userId, User patch){
        User toUpdateUser = userRepository.findById(userId).get();
        toUpdateUser.setName(patch.getName());
        toUpdateUser.setEmail(patch.getEmail());
        return userRepository.save(toUpdateUser);
    }
    public User getUserById(Integer userId){
        return userRepository.findById(userId).orElse(null);
    }
    public List<User> getAllUsers(){
        return (List<User>)userRepository.findAll();
    }
    public void deleteUserById(Integer userId){
        userRepository.deleteById(userId);
    }
    public List<TodoTask> getTasksByUserId(Integer userId){
        return (List<TodoTask>)taskRepository.findTodoTaskByUserId(userId);
    }
}

