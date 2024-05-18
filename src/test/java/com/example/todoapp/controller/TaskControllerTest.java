package com.example.todoapp.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.todoapp.exceptions.TaskNotFoundException;
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
    @Autowired
    private ObjectMapper objectMapper;

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
    public void createTask_shouldNotCreateTaskDueToNullUser() throws Exception {
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
    public void updateTask_shouldUpdateTask() throws Exception {
        TodoTask todoTask = new TodoTask("update task", "update description", Priority.MEDIUM, LocalDate.now(),
            Status.DONE);
        todoTask.setId(1);
        when(taskService.updateTask(todoTask.getId(), todoTask)).thenReturn(todoTask);
        mockMvc.perform(post("/tasks/update/{taskId}", todoTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoTask)))
            .andExpect(status().isOk());
    }

    @Test
    public void updateTask_shouldNotUpdateTaskDueToTaskNotFound() throws Exception {
        TodoTask todoTask = new TodoTask("Update task", "Update description", Priority.MEDIUM, LocalDate.now(),
            Status.DONE);
        todoTask.setId(5000);
        when(userService.getUserById(5000)).thenReturn(null);
        when(taskService.updateTask(todoTask.getId(), todoTask)).thenReturn(null);
        mockMvc.perform(post("/tasks/update/{taskId}", todoTask.getId())
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
    public void getTaskById_shouldReturnTaskById() throws Exception {
        TodoTask todoTask = new TodoTask();
        todoTask.setId(1);
        when(taskService.getTaskById(todoTask.getId())).thenReturn(todoTask);
        mockMvc.perform(get("/tasks/")
            .param("id", String.valueOf(todoTask.getId()))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getTaskById_shouldNotReturnTaskDueToNotFoundTaskId() throws Exception {
        int notExistentTaskId = 5000;
        when(taskService.getTaskById(notExistentTaskId)).thenThrow(TaskNotFoundException.class);
        mockMvc.perform(get("/tasks") // "/tasks" & "/tasks/"
            .param("id", String.valueOf(notExistentTaskId))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void removeTask_shouldRemoveTaskById() throws Exception {
        TodoTask todoTask = new TodoTask();
        todoTask.setId(1);
        doNothing().when(taskService).deleteTaskById(todoTask.getId());
        mockMvc.perform(delete("/tasks/remove")
            .param("id", String.valueOf(todoTask.getId()))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    @Test
    public void removeTask_shouldNotReturnDueToNotFoundTaskId() throws Exception {
        int notExistentTaskId = 5000;
        doThrow(new TaskNotFoundException("Not Found Task")).when(taskService).deleteTaskById(notExistentTaskId);
        mockMvc.perform(delete("/tasks/remove")
            .param("id", String.valueOf(notExistentTaskId))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void getTaskByPriority_shouldReturnListOfTaskByPriority() throws Exception {
        List<TodoTask> taskList = List.of(new TodoTask());
        Priority priorityTest = Priority.MEDIUM;
        when(taskService.getTaskByPriority(priorityTest)).thenReturn(taskList);
        mockMvc.perform(get("/tasks/filter/priority")
            .param("priority", priorityTest.toString())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getTaskByPriority_shouldNotReturnListOfTaskDueToNotFoundPriority() throws Exception {
        Priority priorityTest = Priority.MEDIUM;
        when(taskService.getTaskByPriority(priorityTest)).thenThrow(
            new TaskNotFoundException("No Tasks of " + priorityTest));
        mockMvc.perform(get("/tasks/filter/priority")
            .param("priority", priorityTest.toString())
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void getTaskByDeadline_shouldReturnListOfTaskByDeadline() throws Exception {
        List<TodoTask> taskList = List.of(new TodoTask());
        LocalDate deadline = LocalDate.now();
        when(taskService.getTaskByDeadline(deadline)).thenReturn(taskList);
        mockMvc.perform(get("/tasks/filter/deadline")
            .param("deadline", String.valueOf(deadline))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getTaskByDeadline_shouldNotReturnListOfTaskDueToNotFoundDeadline() throws Exception {
        LocalDate deadline = LocalDate.now();
        when(taskService.getTaskByDeadline(deadline)).thenThrow(new TaskNotFoundException("No Tasks of " + deadline));
        mockMvc.perform(get("/tasks/filter/deadline")
            .param("deadline", String.valueOf(deadline))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void getTaskByStatus_shouldReturnListOfTaskByStatus() throws Exception {
        List<TodoTask> taskList = List.of(new TodoTask());
        Status status = Status.TO_DO;
        when(taskService.getTaskByStatus(status)).thenReturn(taskList);
        mockMvc.perform(get("/tasks/filter/status")
            .param("status", String.valueOf(status))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getTaskByStatus_shouldNotReturnListOfTaskDueToNotFoundStatus() throws Exception {
        Status status = Status.TO_DO;
        when(taskService.getTaskByStatus(status)).thenThrow(new TaskNotFoundException("Not Found tasks of" + status));
        mockMvc.perform(get("/tasks/filter/status")
            .param("status", String.valueOf(status))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void getAllTasksByUserId_shouldReturnListOfTaskByUser() throws Exception {
        List<TodoTask> taskList = List.of(new TodoTask());
        User user = new User();
        user.setId(1);
        when(userService.getTasksByUserId(user.getId())).thenReturn(taskList);
        mockMvc.perform(get("/tasks/filter/user")
            .param("userId", String.valueOf(user.getId()))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getAllTasksByUserId_shouldNotReturnListOfTaskDueToNotFoundUser() throws Exception {
        int notExistentUserId = 5000;
        when(userService.getTasksByUserId(notExistentUserId)).thenThrow(new UserNotFoundException("No Found User"));
        mockMvc.perform(get("/tasks/filter/user")
            .param("userId", String.valueOf(notExistentUserId))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

}
