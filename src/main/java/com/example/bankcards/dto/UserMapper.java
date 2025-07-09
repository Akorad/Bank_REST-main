package com.example.bankcards.dto;

import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserRegisterRequest request);

    UserResponse toDto(User user);
}
