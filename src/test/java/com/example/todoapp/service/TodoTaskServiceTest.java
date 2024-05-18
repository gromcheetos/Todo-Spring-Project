package com.example.todoapp.service;

import com.example.todoapp.exceptions.TaskNotFoundException;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.model.enums.Priority;
import com.example.todoapp.model.enums.Status;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.mockito.BDDMockito.given;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TodoTaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TodoTaskService taskService;

    @Test
    public void getAllTasks_shouldReturnList(){
        List<TodoTask> expectedList = Arrays.asList(new TodoTask(1,"Some task", "Some description", Priority.MEDIUM,
                LocalDate.now(), Status.IN_PROGRESS));
        when(taskRepository.findAll()).thenReturn(expectedList);
        List<TodoTask> actualList = taskService.getAllTasks();
        Assertions.assertNotNull(actualList);
        Assertions.assertFalse(actualList.isEmpty());
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals("Some task", actualList.get(0).getTitle());
        Assertions.assertEquals(1, actualList.get(0).getId());
        verify(taskRepository).findAll();
    }
    @Test
    public void insertTask_shouldCreateTask(){
        TodoTask task = new TodoTask();
        task.setId(1);
        when(taskRepository.save(task)).thenReturn(task);
        TodoTask actualInsertedTask = taskService.insertTask(task);

        Assertions.assertNotNull(actualInsertedTask);
        Assertions.assertEquals(1, task.getId());
        verify(taskRepository).save(task);
    }
    @Test
    public void updateTask_shouldUpdateTask() throws TaskNotFoundException {
        Integer taskId = 1;
        TodoTask task = new TodoTask();
        task.setId(taskId);
        task.setTitle("Go To Council");

        TodoTask updatedTask = new TodoTask();
        updatedTask.setId(1);
        updatedTask.setTitle("Go To City hall");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(updatedTask);

        TodoTask actualResult = taskService.updateTask(taskId, task);

        Assertions.assertNotNull(actualResult);
        Assertions.assertNotEquals(task.getTitle(), actualResult.getTitle());
        Assertions.assertEquals(taskId, actualResult.getId());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(task);
    }
    @Test
    public void updateTask_shouldNotUpdateTaskDueToNullUser() {
        Integer noExistentTaskId = 5000;
        TodoTask task = new TodoTask();
        task.setId(noExistentTaskId);

        when(taskRepository.findById(noExistentTaskId)).thenReturn(Optional.empty());

        TaskNotFoundException thrownException = Assertions.assertThrows(TaskNotFoundException.class, () ->{
            taskService.updateTask(noExistentTaskId, task);
        });

        Assertions.assertEquals("No Found Task", thrownException.getMessage());

        verify(taskRepository).findById(noExistentTaskId);
        verify(taskRepository, never()).save(task);
    }
    @Test
    public void getTaskById_shouldReturnTask() throws TaskNotFoundException {
        Integer taskId = 1;
        TodoTask task = new TodoTask();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        TodoTask actualResult = taskService.getTaskById(taskId);

        Assertions.assertNotNull(actualResult);
        Assertions.assertEquals(task.getId(), actualResult.getId());
        verify(taskRepository, times(1)).findById(taskId);
    }
    @Test
    public void getTaskById_shouldReturnTaskDueToNotFoundTask(){
        Integer noExistentTaskId = 5000;

        when(taskRepository.findById(noExistentTaskId)).thenReturn(Optional.empty());

        TaskNotFoundException thrownException = Assertions.assertThrows(TaskNotFoundException.class, () ->{
            taskService.getTaskById(noExistentTaskId);
        });

        Assertions.assertEquals("No Found Task", thrownException.getMessage());
        verify(taskRepository).findById(noExistentTaskId);
    }
    @Test
    public void deleteTaskById_shouldDeleteTask() throws TaskNotFoundException {
        Integer taskId = 1;
        TodoTask task = new TodoTask();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById(task.getId());
        taskService.deleteTaskById(taskId);
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }
    @Test
    public void deleteTaskById_shouldNotDeleteDueToNotFoundTask(){
        Integer noExistentTaskId = 5000;

        when(taskRepository.findById(noExistentTaskId)).thenReturn(Optional.empty());

        TaskNotFoundException thrownException = Assertions.assertThrows(TaskNotFoundException.class, () ->{
            taskService.deleteTaskById(noExistentTaskId);
        });

        Assertions.assertEquals("No Found Task", thrownException.getMessage());

        verify(taskRepository).findById(noExistentTaskId);
        verify(taskRepository, never()).deleteById(noExistentTaskId);
    }
    @Test
    public void getTaskByPriority_shouldReturnList() throws TaskNotFoundException {
        List<TodoTask> listByPriority = List.of(new TodoTask());
        Priority priority = Priority.LOW;
        when(taskRepository.findByPriority(priority)).thenReturn(listByPriority);
        List<TodoTask> actualList = taskService.getTaskByPriority(priority);

        Assertions.assertNotNull(actualList);
        Assertions.assertEquals(1, actualList.size());
        verify(taskRepository).findByPriority(priority);
    }
    @Test
    public void getTaskByPriority_shouldNotReturnDueToNotFoundTask(){
        Priority priority = Priority.MEDIUM;

        when(taskRepository.findByPriority(priority)).thenReturn(Collections.emptyList());
        TaskNotFoundException thrownException = Assertions.assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskByPriority(priority);
        });

        Assertions.assertEquals("No Found Task", thrownException.getMessage());
        verify(taskRepository).findByPriority(priority);
    }
    @Test
    public void getTaskByDeadline_shouldReturnList() throws TaskNotFoundException {
        List<TodoTask> listByDeadline = List.of(new TodoTask());
        LocalDate deadline = LocalDate.now();
        when(taskRepository.findByDeadline(deadline)).thenReturn(listByDeadline);
        List<TodoTask> actualList = taskService.getTaskByDeadline(deadline);

        Assertions.assertNotNull(actualList);
        Assertions.assertEquals(1, actualList.size());
        verify(taskRepository).findByDeadline(deadline);
    }
    @Test
    public void getTaskByDeadline_shouldNotReturnDueToNotFoundTask(){
        LocalDate deadline = LocalDate.now();

        when(taskRepository.findByDeadline(deadline)).thenReturn(Collections.emptyList());
        TaskNotFoundException thrownException = Assertions.assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskByDeadline(deadline);
        });

        Assertions.assertEquals("No Found Task", thrownException.getMessage());
        verify(taskRepository).findByDeadline(deadline);
    }
    @Test
    public void getTaskByStatus_shouldReturnList() throws TaskNotFoundException {
        List<TodoTask> listByStatus = List.of(new TodoTask());
        Status status = Status.IN_PROGRESS;
        when(taskRepository.findByStatus(status)).thenReturn(listByStatus);
        List<TodoTask> actualList = taskService.getTaskByStatus(status);
        Assertions.assertNotNull(actualList);
        verify(taskRepository).findByStatus(status);
    }
    @Test
    public void getTaskByStatus_shouldNotReturnDueToNotFoundTask(){
        Status NotExistentStatus = Status.DONE;
        when(taskRepository.findByStatus(NotExistentStatus)).thenReturn(Collections.emptyList());
        TaskNotFoundException thrownException = Assertions.assertThrows(TaskNotFoundException.class, () ->{
            taskService.getTaskByStatus(NotExistentStatus);
        });
        Assertions.assertEquals("No Found Task", thrownException.getMessage());
        verify(taskRepository).findByStatus(NotExistentStatus);
    }
}
