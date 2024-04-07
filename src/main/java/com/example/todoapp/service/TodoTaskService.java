package com.example.todoapp.service;

import com.example.todoapp.model.TodoTask;
import com.example.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoTaskService {
    @Autowired // creates and destroys objects for you, you don't need to instantiate them
    private TaskRepository taskRepository;

    public void saveOrUpdateTask(TodoTask task){
        taskRepository.save(task); //runs the insert into table todo_tasks values(...)
    }
    public TodoTask getTaskById(Integer taskId){
        return taskRepository.findById(taskId).get();
    }
    public List<TodoTask> getAllTasks(){
        return (List<TodoTask>)taskRepository.findAll();
    }
    public void deleteTaskById(Integer taskId){
        taskRepository.deleteById(taskId);
    }

}
