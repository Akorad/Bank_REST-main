package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
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
}
