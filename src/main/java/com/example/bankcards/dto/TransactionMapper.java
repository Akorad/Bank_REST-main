package com.example.bankcards.dto;

import com.example.bankcards.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "fromCard", ignore = true)
    @Mapping(target = "toCard", ignore = true)
    Transaction toEntity(TransactionRequestDTO request);

    @Mapping(target = "fromCardNumberMasked", expression = "java(com.example.bankcards.util.CardUtils.maskCardNumber(transaction.getFromCard().getNumber()))")
    @Mapping(target = "toCardNumberMasked", expression = "java(com.example.bankcards.util.CardUtils.maskCardNumber(transaction.getToCard().getNumber()))")
    @Mapping(target = "createdAt", expression = "java(transaction.getCreatedAt().toString())")
    TransactionResponseDTO toDto(Transaction transaction);
}
