package com.example.bankcards.service;

import com.example.bankcards.dto.JwtResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.UserRegisterRequest;
import com.example.bankcards.dto.UserResponse;

public interface AuthService {
    UserResponse register(UserRegisterRequest request);
    JwtResponse login (LoginRequest request);
}
