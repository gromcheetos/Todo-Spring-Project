package com.example.todoapp.service;

import com.example.todoapp.exceptions.TaskNotFoundException;
import com.example.todoapp.exceptions.UserNotFoundException;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.model.User;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public User createUser(User user, String password) {
        user.setPassword(encoder.encode(password));
        return userRepository.save(user);
    }

    public User updateUserById(Integer userId, String name, String email, String username, String password) throws UserNotFoundException {
        User toUpdateUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No Found User"));
        toUpdateUser.setName(name);
        toUpdateUser.setEmail(email);
        toUpdateUser.setUsername(username);
        toUpdateUser.setPassword(password);
        return userRepository.save(toUpdateUser);
    }

    public User getUserById(Integer userId) throws UserNotFoundException{
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No Found User"));
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void deleteUserById(Integer userId) throws UserNotFoundException{
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException("No Found User");
        }
        userRepository.deleteById(userId);
    }

    public List<TodoTask> getTasksByUserId(Integer userId) throws UserNotFoundException {
        List<TodoTask> tasks = taskRepository.findTodoTaskByUserId(userId);
        if(tasks.isEmpty()){
            throw new UserNotFoundException("No Found User");
        }
        return tasks;
    }

    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

}

