package com.example.todoapp.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.todoapp.exceptions.UserNotFoundException;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.model.User;
import com.example.todoapp.model.enums.Priority;
import com.example.todoapp.model.enums.Status;
import com.example.todoapp.service.TodoTaskService;
import com.example.todoapp.service.UserService;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class TaskControllerTest {

    @MockBean
    private TodoTaskService taskService;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllTasks_shouldReturnListOfTasks() throws Exception {
        // when the endpoint /list is executed -> return this "fake" list
        // then we will call the endpoint /list and see what happens
        List<TodoTask> taskList = List.of(new TodoTask());
        when(taskService.getAllTasks()).thenReturn(taskList);
        mockMvc.perform(get("/tasks/list")).andExpect(status().isOk());
    }
    @Test
    public void createTask_shouldCreateTask() throws Exception {
        User user = new User();
        user.setId(1);
        TodoTask todoTask = new TodoTask("Some task", "Some description", Priority.MEDIUM, LocalDate.now(),
            Status.IN_PROGRESS);
        todoTask.setId(1);
        todoTask.setUser(user);

        when(userService.getUserById(1)).thenReturn(user);
        when(taskService.insertTask(todoTask)).thenReturn(todoTask);

        mockMvc.perform(post("/tasks/create")
            .param("title", "Some task")
            .param("description", "Some description")
            .param("priority", Priority.MEDIUM.toString())
            .param("deadline", LocalDate.now().toString())
            .param("status", Status.IN_PROGRESS.toString())
            .param("userId", "1")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }
    @Test
    public void createTask_shouldNotCreateTaskDueToNullUser() throws Exception{
        User user = new User();
        user.setId(5000);
        TodoTask todoTask = new TodoTask("Some task", "Some description", Priority.MEDIUM, LocalDate.now(),
                Status.IN_PROGRESS);
        todoTask.setId(1);
        todoTask.setUser(user);

        //when(userService.getUserById(userId)).thenThrow(UserNotFoundException.class);
        when(userService.getUserById(5000)).thenReturn(null);
        when(taskService.insertTask(todoTask)).thenReturn(todoTask);
        mockMvc.perform(post("/tasks/create")
                .param("title", "Some task")
                .param("description", "Some description")
                .param("priority", Priority.MEDIUM.toString())
                .param("deadline", LocalDate.now().toString())
                .param("status", Status.IN_PROGRESS.toString())
                .param("userId", String.valueOf(user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
    @Test
    public void updateTask_shouldUpdateTask() throws Exception{
        TodoTask todoTask = new TodoTask("update task", "update description", Priority.MEDIUM, LocalDate.now(),
                Status.DONE);
        todoTask.setId(1);
        when(taskService.updateTask(todoTask.getId(), todoTask)).thenReturn(todoTask);
        mockMvc.perform(post("/update/{taskId}", todoTask.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void updateTask_shouldNotUpdateTaskDueToTaskNotFound() throws Exception{
        TodoTask todoTask = new TodoTask("Update task", "Update description", Priority.MEDIUM, LocalDate.now(),
                Status.DONE);
        todoTask.setId(5000);
        when(userService.getUserById(5000)).thenReturn(null);
        when(taskService.updateTask(todoTask.getId(), todoTask)).thenReturn(null);
        mockMvc.perform(post("/update/{taskId}", todoTask.getId())
                .param("title", "Update task")
                .param("description", "Update description")
                .param("priority", Priority.MEDIUM.toString())
                .param("deadline", LocalDate.now().toString())
                .param("status", Status.DONE.toString())
                .param("taskId", String.valueOf(todoTask.getId()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void getTaskById_shouldReturnListOfTaskById(){

    }

    @Test
    public void getTaskById_shouldNotReturnListOfTaskDueToNotFoundTaskId(){

    }

    @Test
    public void removeTask_shouldRemoveTaskById(){

    }

    @Test
    public void removeTask_shouldNotRemoveTaskDueToNotFoundTaskId(){

    }

    @Test
    public void getTaskByPriority_shouldReturnListOfTaskByPriority(){

    }
    @Test
    public void getTaskByPriority_shouldNotReturnListOfTaskDueToNotFoundPriority(){

    }
    @Test
    public void getTaskByDeadline_shouldReturnListOfTaskByDeadline(){

    }
    @Test
    public void getTaskByDeadline_shouldNotReturnListOfTaskDueToNotFoundDeadline(){

    }
    @Test
    public void getTaskByStatus_shouldReturnListOfTaskByStatus(){

    }
    @Test
    public void getTaskByDeadline_shouldNotReturnListOfTaskDueToNotFoundStatus(){

    }
    @Test
    public void getAllTasksByUserId_shouldReturnLisgOfTaskByUser(){

    }
    @Test
    public void getTaskByDeadline_shouldNotReturnListOfTaskDueToNotFoundUser(){

    }

}
