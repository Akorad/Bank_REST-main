package com.example.bankcards.dto;

import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "maskedNumber", expression = "java(maskCardNumber(card.getNumber()))")
    CardResponseDTO toDto(Card card);

    Card toEntity(CardRequestDto request);

    default String maskCardNumber(String encryptedNumber) {
        return "**** **** **** " + encryptedNumber.substring(encryptedNumber.length() - 4);
    }
}
