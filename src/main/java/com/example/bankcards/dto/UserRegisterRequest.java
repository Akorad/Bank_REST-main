package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на регистрацию пользователя")
public class UserRegisterRequest extends AuthRequest {
}
