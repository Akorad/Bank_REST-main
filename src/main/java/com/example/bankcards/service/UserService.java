package com.example.bankcards.service;

import com.example.bankcards.dto.UserRegisterRequest;
import com.example.bankcards.dto.UserResponse;
import com.example.bankcards.entity.User;

import java.util.Optional;

public interface UserService {
    UserResponse register(UserRegisterRequest request);
    Optional<UserResponse> findByUsername(String username);
    Optional<User> findEntityByUserName(String username);
    User getCurrentUser();
}
