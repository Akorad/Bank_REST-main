package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Ответ с информацией о пользователе")
public class UserResponse {

    @Schema(description = "Уникальный идентификатор пользователя", example = "123")
    private Long id;

    @Schema(description = "Имя пользователя (логин)", example = "vladislav")
    private String username;

    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;

    @Schema(description = "Имя пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    private String lastName;
}
