package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
}
