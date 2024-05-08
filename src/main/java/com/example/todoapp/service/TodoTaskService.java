package com.example.todoapp.service;

import com.example.todoapp.model.enums.Priority;
import com.example.todoapp.model.enums.Status;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoTaskService {
    @Autowired // creates and destroys objects for you, you don't need to instantiate them
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public TodoTask insertTask(TodoTask task){
        return taskRepository.save(task); //runs the insert into table todo_tasks values(...)
    }
    public TodoTask updateTask(Integer taskId, TodoTask todoTask){
        TodoTask toUpdateTask = taskRepository.findById(taskId).orElseThrow(NoSuchElementException :: new);
        toUpdateTask.setTitle(todoTask.getTitle());
        toUpdateTask.setDeadline(todoTask.getDeadline());
        toUpdateTask.setPriority(todoTask.getPriority());
        return taskRepository.save(toUpdateTask);
    }
    public TodoTask updateTaskById(Integer taskId, String title, Priority priority, LocalDate deadline){
        TodoTask toUpdateTask = taskRepository.findById(taskId).orElseThrow(NoSuchElementException :: new);
        toUpdateTask.setTitle(title);
        toUpdateTask.setPriority(priority);
        toUpdateTask.setDeadline(deadline);
        return taskRepository.save(toUpdateTask);
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
