package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AuthRequest {
    @NotBlank
    @Size(min = 4,max = 30)
    private String username;

    @NotBlank
    @Size(min = 6,max = 64)
    private String password;
}
