package com.example.bankcards.service;

import com.example.bankcards.dto.JwtResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.security.JwtTokenProvider;
import com.example.bankcards.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest loginRequest;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("password");

        GrantedAuthority authority = () -> "ROLE_USER";

        authentication = new UsernamePasswordAuthenticationToken(
                "testUser", null, Collections.singletonList(authority)
        );
    }

    @Test
    void login_ShouldReturnJwtResponse_WhenCredentialsAreValid() {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken("testUser", "ROLE_USER")).thenReturn("fake-jwt-token");

        JwtResponse jwtResponse = authService.login(loginRequest);

        assertNotNull(jwtResponse);
        assertEquals("fake-jwt-token", jwtResponse.getToken());

        verify(authenticationManager).authenticate(any());
        verify(jwtTokenProvider).generateToken("testUser", "ROLE_USER");
    }

    @Test
    void login_ShouldThrowException_WhenAuthenticationFails() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Bad credentials"));

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));

        verify(authenticationManager).authenticate(any());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString());
    }
}
