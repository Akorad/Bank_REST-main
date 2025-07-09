package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AuthRequest {
    @Schema(
            description = "Имя пользователя (уникальное)",
            example = "vlad",
            minLength = 4,
            maxLength = 30
    )
    @NotBlank
    @Size(min = 4,max = 30)
    private String username;

    @Schema(
            description = "Пароль (минимум 6 символов)",
            example = "qwerty123",
            minLength = 6,
            maxLength = 64
    )
    @NotBlank
    @Size(min = 6,max = 64)
    private String password;
}
