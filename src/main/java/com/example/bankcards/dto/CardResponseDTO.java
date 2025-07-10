package com.example.bankcards.dto;

import com.example.bankcards.entity.CardStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ с данными карты")
public class CardResponseDTO {

    @Schema(description = "Идентификатор карты", example = "1")
    private Long id;

    @Schema(description = "Маскированный номер карты", example = "**** **** **** 1234")
    private String number;

    @Schema(description = "Срок действия карты", example = "12/25")
    private String expirationDate;

    @Schema(description = "Имя владельца", example = "IVAN IVANOV")
    private String ownerName;

    @Schema(description = "Статус карты", example = "ACTIVE")
    private CardStatus status;

    @Schema(description = "Баланс карты>", example = "500.00")
    private Double balance;
}
