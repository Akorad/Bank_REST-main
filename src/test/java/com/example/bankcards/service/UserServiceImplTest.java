package com.example.bankcards.service;

import com.example.bankcards.dto.UserMapper;
import com.example.bankcards.dto.UserRegisterRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserAlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterRequest registerRequest;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("password");

        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setEnabled(true);

        userResponse = new UserResponse();
        userResponse.setUsername("testUser");
    }

    @Test
    void register_ShouldSaveUser_WhenUsernameIsUnique(){
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userMapper.toEntity(registerRequest)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodePassword");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponse);

        UserResponse response = userService.register(registerRequest);

        assertNotNull(response);
        assertEquals("testUser",response.getUsername());

        verify(userRepository).existsByUsername("testUser");
        verify(userMapper).toEntity(registerRequest);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists(){
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, ()->{
            userService.register(registerRequest);
        });

        verify(userRepository).existsByUsername("testUser");
        verify(userMapper, never()).toEntity(any());
        verify(userRepository, never()).save(any());
    }
}
