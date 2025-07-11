package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardResponseDTO toDto(Card card);

}
