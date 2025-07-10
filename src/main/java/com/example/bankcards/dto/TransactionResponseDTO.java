package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ о совершённой транзакции")
public class TransactionResponseDTO {

    @Schema(description = "Идентификатор транзакции", example = "101")
    private Long id;

    @Schema(description = "Маскированный номер карты отправителя", example = "**** **** **** 1234")
    private String fromCardNumberMasked;

    @Schema(description = "Маскированный номер карты получателя", example = "**** **** **** 5678")
    private String toCardNumberMasked;

    @Schema(description = "Сумма перевода", example = "1500.00")
    private Double amount;

    @Schema(description = "Дата и время перевода", example = "2025-07-10T12:34:56")
    private String createdAt;
}
