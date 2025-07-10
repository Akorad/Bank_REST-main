package com.example.bankcards.controller;

import com.example.bankcards.dto.TransactionRequestDTO;
import com.example.bankcards.dto.TransactionResponseDTO;
import com.example.bankcards.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "Управление переводами между картами")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Выполнить перевод", description = "Перевод средств между картами")
    public TransactionResponseDTO transfer(@RequestBody TransactionRequestDTO request) {
        return transactionService.transfer(request);
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Получить историю переводов",description = "Возвращает историю переводов, где текущий пользователь — отправитель или получатель")
    public Page<TransactionResponseDTO> getHistory(@ParameterObject Pageable pageable) {
        return transactionService.getTransactionHistory(pageable);
    }
}