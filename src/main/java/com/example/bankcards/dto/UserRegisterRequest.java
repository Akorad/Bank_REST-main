package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на регистрацию пользователя")
public class UserRegisterRequest extends AuthRequest {

    @Schema(description = "Имя пользователя", example = "Иван")
    @NotBlank
    @Size(min = 1,max = 64)
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Иванов")
    @NotBlank
    @Size(min = 1,max = 64)
    private String lastName;
}
