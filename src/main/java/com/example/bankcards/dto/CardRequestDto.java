package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "Запрос на создание карты")
public class CardRequestDto {

    @Schema(description = "Номер карты", example = "1234567812345678")
    private String number;

    @Schema(description = "Срок действия (MM/YY)", example = "12/25")
    private String expirationDate;

    @Schema(description = "Имя владельца (как на карте)", example = "IVAN IVANOV")
    private String ownerName;
}
