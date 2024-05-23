package com.example.todoapp.controller;

import com.example.todoapp.exceptions.TaskNotFoundException;
import com.example.todoapp.exceptions.UserNotFoundException;
import com.example.todoapp.model.User;
import com.example.todoapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllUsersTest() throws Exception {
        List<User> userList = List.of(new User());
        when(userService.getAllUsers()).thenReturn(userList);
        mockMvc.perform(get("/users/list")).andExpect(status().isOk());
    }

    @Test
    public void createUserTest() throws Exception {
        User user = new User("Mary", "mary@hotmail.com", "coffeeLover", "1234");
        user.setId(1);
        when(userService.createUser(user, user.getPassword())).thenReturn(user);
        mockMvc.perform(post("/users/register")
            .param("name", "Mary")
            .param("userEmail", "mary@hotmail.com")
            .param("username", "coffeeLover")
            .param("password", "1234")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void updateUserTest() throws Exception {
        User user = new User("Mary", "mary@hotmail.com", "coffeeLover", "1234");
        user.setId(1);
        when(userService.updateUserById(user.getId(), user.getName(), user.getEmail(),user.getUsername(), user.getPassword())).thenReturn(user);
        mockMvc.perform(post("/users/update")
            .param("userId", String.valueOf(user.getId()))
            .param("name", "Mary")
            .param("userEmail", "mary@changedEmail.net")
            .param("username", "teaLover")
            .param("password", "1234")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

    }

    @Test
    public void updateUserTest_shouldNotUpdateDueToNullUser() throws Exception {
        int noExistentUserId = 5000;
        when(userService.updateUserById(noExistentUserId, "Mary", "mary@changedEmail.net", "coffeeLover", "1234")).thenThrow(
            UserNotFoundException.class);
        mockMvc.perform(post("/users/update")
            .param("userId", String.valueOf(noExistentUserId))
            .param("name", "Mary")
            .param("userEmail", "mary@changedEmail.net")
            .param("username", "teaLover")
            .param("password", "1234")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void deleteUserTest() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.getUserById(1)).thenReturn(user);
        mockMvc.perform(delete("/users/delete")
            .param("userId", String.valueOf(user.getId()))
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteUserTest_shouldNotReturnDueToNullUser() throws Exception {
        int notExistentUserId = 5000;
        doThrow(new UserNotFoundException("No Valid User")).when(userService).deleteUserById(notExistentUserId);
        mockMvc.perform(delete("/users/delete")
            .param("userId", String.valueOf(notExistentUserId))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        User user = new User();
        user.setId(1);
        when(userService.getUserById(user.getId())).thenReturn(user);
        mockMvc.perform(get("/users/search")
            .param("userId", String.valueOf(user.getId()))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void getUserByIdTest_shouldNotReturnDueToNullUser() throws Exception {
        int notExistentUserId = 5000;
        when(userService.getUserById(notExistentUserId)).thenThrow(new UserNotFoundException("No Valid User"));
        mockMvc.perform(get("/users/search")
            .param("userId", String.valueOf(notExistentUserId))
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}
