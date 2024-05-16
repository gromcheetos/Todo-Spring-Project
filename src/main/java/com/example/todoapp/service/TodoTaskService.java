package com.example.todoapp.service;

import com.example.todoapp.controller.TaskController;
import com.example.todoapp.exceptions.TaskNotFoundException;
import com.example.todoapp.exceptions.UserNotFoundException;
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
    public TodoTask updateTask(Integer taskId, TodoTask todoTask) throws TaskNotFoundException {
        TodoTask toUpdateTask = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("No Found Task"));
        toUpdateTask.setTitle(todoTask.getTitle());
        toUpdateTask.setDescription(todoTask.getDescription());
        toUpdateTask.setDeadline(todoTask.getDeadline());
        toUpdateTask.setPriority(todoTask.getPriority());
        toUpdateTask.setStatus(todoTask.getStatus());
        return taskRepository.save(toUpdateTask);
    }
    /*public TodoTask updateTaskById(Integer taskId, String title, Priority priority, LocalDate deadline){
        TodoTask toUpdateTask = taskRepository.findById(taskId).orElseThrow(NoSuchElementException :: new);
        toUpdateTask.setTitle(title);
        toUpdateTask.setPriority(priority);
        toUpdateTask.setDeadline(deadline);
        return taskRepository.save(toUpdateTask);
    }*/

    public TodoTask getTaskById(Integer taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("No Found Task"));
    }
    public List<TodoTask> getAllTasks(){
        return (List<TodoTask>)taskRepository.findAll();
    }
    public void deleteTaskById(Integer taskId)  throws TaskNotFoundException {
        if(taskRepository.findById(taskId).isEmpty()){
            throw new TaskNotFoundException("No Found Task");
        }
        taskRepository.deleteById(taskId);
    }
    public List<TodoTask> getTaskByPriority(Priority priority) throws TaskNotFoundException{
        List<TodoTask> tasks = taskRepository.findByPriority(priority);
        if(tasks.isEmpty()){
            throw new TaskNotFoundException("No Found Task");
        }
        return tasks;
    }
    public List<TodoTask> getTaskByDeadline(LocalDate deadline) throws TaskNotFoundException{
        List<TodoTask> tasks = taskRepository.findByDeadline(deadline);
        if(tasks.isEmpty()){
            throw new TaskNotFoundException("No Found Task");
        }
        return tasks;
    }
    public List<TodoTask> getTaskByStatus(Status status) throws TaskNotFoundException{
        List<TodoTask> tasks = taskRepository.findByStatus(status);
        if(tasks.isEmpty()){
            throw new TaskNotFoundException("No Found Task");
        }
        return tasks;
    }
    /*
    public Status getStatusFromUserStatus(String userStatus){
        for (Status status : Status.values()) {
            if (status.getUserStatus().equalsIgnoreCase(userStatus)) {
                return status;
            }
        }
        return null;
    }*/
}
