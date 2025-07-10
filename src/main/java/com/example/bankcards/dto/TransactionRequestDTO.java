package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на перевод между картами")
public class TransactionRequestDTO {

    @Schema(description = "ID карты отправителя", example = "1")
    private Long fromCardId;

    @Schema(description = "ID карты получателя", example = "2")
    private Long toCardId;

    @Schema(description = "Сумма перевода", example = "1500.00")
    private Double amount;
}
