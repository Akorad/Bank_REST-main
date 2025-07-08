package com.example.bankcards.dto;

import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRegisterRequest request);
    UserResponse toDto(User user);
}
