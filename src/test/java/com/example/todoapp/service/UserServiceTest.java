package com.example.todoapp.service;

import com.example.todoapp.exceptions.UserNotFoundException;
import com.example.todoapp.model.TodoTask;
import com.example.todoapp.model.User;
import com.example.todoapp.repository.TaskRepository;
import com.example.todoapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void getAllUsersTest(){
        List<User> expectedList = Arrays.asList(new User("James","james@bond.net", "any"));
        when(userRepository.findAll()).thenReturn(expectedList);
        List<User> actualList = userService.getAllUsers();

        Assertions.assertNotNull(actualList);
        Assertions.assertFalse(actualList.isEmpty());
        Assertions.assertEquals(1, actualList.size());
        Assertions.assertEquals("James", actualList.get(0).getName());
        Assertions.assertEquals(1, actualList.get(0).getId());
        verify(userRepository).findAll();
    }
    @Test
    public void createUserTest(){
        User user = new User();
        String password = "1234";
        when(userRepository.save(user)).thenReturn(user);
        User actualUser = userService.createUser(user, password);
        Assertions.assertNotNull(actualUser);
        verify(userRepository).save(user);
    }
    @Test
    public void updateUserByIdTest() throws UserNotFoundException {
        User user = new User();
        user.setId(1);
        user.setName("Daniel");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User actualUser = userService.updateUserById(user.getId(), "James Bond", "james@bond.net", "any", "1234");
        Assertions.assertNotNull(actualUser);
        Assertions.assertNotEquals("Daniel", actualUser.getName());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(user);
    }
    @Test
    public void updateUserByIdTest_shouldNotReturnDueToNullUser(){
        int noExistentUserId = 5000;
        User user = new User();
        user.setId(noExistentUserId);

        when(userRepository.findById(noExistentUserId)).thenReturn(Optional.empty());

        UserNotFoundException thrownException = Assertions.assertThrows(UserNotFoundException.class, () ->{
            userService.updateUserById(noExistentUserId, "James Bond", "james@bond.net", "007", "1234");
        });

        Assertions.assertEquals("No Found User", thrownException.getMessage());
        verify(userRepository).findById(noExistentUserId);
        verify(userRepository, never()).save(user);
    }
    @Test
    public void getUserByIdTest() throws UserNotFoundException {
        User user = new User();
        user.setId(1);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User actualUser = userService.getUserById(user.getId());
        Assertions.assertNotNull(actualUser);
        verify(userRepository, times(1)).findById(user.getId());
    }
    @Test
    public void getUserByIdTest_shouldNotReturnDueToNullUser(){
        int noExistentUserId = 5000;
        User user = new User();
        user.setId(noExistentUserId);

        when(userRepository.findById(noExistentUserId)).thenReturn(Optional.empty());
        UserNotFoundException thrownException = Assertions.assertThrows(UserNotFoundException.class, () ->{
            userService.getUserById(noExistentUserId);
        });
        Assertions.assertEquals("No Found User", thrownException.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
    }
    @Test
    public void deleteUserByIdTest() throws UserNotFoundException {
        int toDeleteUserId = 1;
        User user = new User();
        user.setId(toDeleteUserId);

        when(userRepository.findById(toDeleteUserId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(toDeleteUserId);
        userService.deleteUserById(toDeleteUserId);
        verify(userRepository, times(1)).findById(toDeleteUserId);
        verify(userRepository).deleteById(toDeleteUserId);
    }
    @Test
    public void deleteUserByIdTest_shouldNotReturnDueToNullUser(){
        int noExistentUserId = 5000;
        User user = new User();
        user.setId(noExistentUserId);

        when(userRepository.findById(noExistentUserId)).thenReturn(Optional.empty());
        UserNotFoundException thrownException = Assertions.assertThrows(UserNotFoundException.class, () ->{
            userService.deleteUserById(noExistentUserId);
        });
        Assertions.assertEquals("No Found User", thrownException.getMessage());
        verify(userRepository).findById(noExistentUserId);
        verify(userRepository, never()).deleteById(noExistentUserId);
    }
    @Test
    public void getTasksByUserIdTest() throws UserNotFoundException {
        List<TodoTask> taskList = List.of(new TodoTask());
        int toFindTasksUserId = 1;

        when(taskRepository.findTodoTaskByUserId(toFindTasksUserId)).thenReturn(taskList);
        List<TodoTask> resultTaskLIst = userService.getTasksByUserId(toFindTasksUserId);

        Assertions.assertNotNull(resultTaskLIst);
        Assertions.assertFalse(resultTaskLIst.isEmpty());
        Assertions.assertEquals(1, resultTaskLIst.size());
        verify(taskRepository).findTodoTaskByUserId(toFindTasksUserId);
    }

    @Test
    public void getTasksByUserIdTest_shouldNotReturnDueToNullUser(){
        List<TodoTask> taskList = List.of(new TodoTask());
        int noExistentUserId = 5000;

        when(taskRepository.findTodoTaskByUserId(noExistentUserId)).thenReturn(Collections.emptyList());
        UserNotFoundException thrownException = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getTasksByUserId(noExistentUserId);
        });

        Assertions.assertEquals("No Found User", thrownException.getMessage());
        verify(taskRepository).findTodoTaskByUserId(noExistentUserId);
    }
}
