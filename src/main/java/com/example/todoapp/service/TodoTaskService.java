package com.example.todoapp.service;

import com.example.todoapp.model.Priority;
import com.example.todoapp.model.Status;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoTaskService {
    @Autowired // creates and destroys objects for you, you don't need to instantiate them
    private TaskRepository taskRepository;

    public void saveOrUpdateTask(TodoTask task){
        taskRepository.save(task); //runs the insert into table todo_tasks values(...)
    }
    public TodoTask getTaskById(Integer taskId){
        return taskRepository.findById(taskId).orElse(null);
    }
    public List<TodoTask> getAllTasks(){
        return (List<TodoTask>)taskRepository.findAll();
    }
    public void deleteTaskById(Integer taskId){
        taskRepository.deleteById(taskId);
    }
    public List<TodoTask> getTaskByPriority(Priority priority){
        return taskRepository.findByPriority(priority);
    }
    public List<TodoTask> getTaskByDeadline(LocalDate deadline){
        return taskRepository.findByDeadline(deadline);
    }
    public List<TodoTask> getTaskByStatus(Status status){
        return taskRepository.findByStatus(status);
    }
    public Status getStatusFromUserStatus(String userStatus) {
        for (Status status : Status.values()) {
            if (status.getUserStatus().equalsIgnoreCase(userStatus)) {
                return status;
            }
        }
        return null;
    }
}
