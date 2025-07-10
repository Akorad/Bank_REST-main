package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardResponseDTO toDto(Card card);

    Card toEntity(CardRequestDto request);
}
