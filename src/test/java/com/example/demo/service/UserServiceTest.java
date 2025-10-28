package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@mail.com");
        user.setRole(Role.ROLE_USER);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("username");
        userDTO.setEmail("email@mail.com");
        userDTO.setRole(Role.ROLE_USER);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldBeDeleteUser() throws UserNotFoundException {
        when(userRepository.existsById(user.getId())).thenReturn(true);

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() throws UserNotFoundException {
        when(!userRepository.existsById(user.getId())).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(user.getId()));

        assertEquals("User with id " + user.getId() + " not found!", exception.getMessage());
        verify(userRepository, never()).deleteById(user.getId());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUserDTO() throws UserNotFoundException {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserById(user.getId());

        assertEquals(userDTO.getUsername(), user.getUsername());
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() throws UserNotFoundException {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(user.getId()));

        assertEquals("User with id " + user.getId() + " not found!", exception.getMessage());
    }

    @Test
    void getUserByUsername_WhenUserExists_ShouldReturnUser() throws UserNotFoundException {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User testUser = userService.getUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), testUser.getUsername());
    }

    @Test
    void getUserByUsername_WhenUserDoesNotExist_ShouldThrowUserNotFoundException() throws UserNotFoundException {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.getUserByUsername(user.getUsername()));

        assertEquals("User with name " + user.getUsername() + " not found!", exception.getMessage());
    }

    @Test
    void getAllUsers_ShouldReturnUsersList() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserDTO> userDTOList = userService.getAllUsers();

        assertEquals(userDTOList.get(0).getId(), user.getId());
    }
}
