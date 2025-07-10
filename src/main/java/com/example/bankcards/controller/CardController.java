package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
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
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = "Card Controller", description = "Управление банковскими картами")
@SecurityRequirement(name = "bearerAuth")
public class CardController {

    private final CardService cardService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Создать карту", description = "Доступно только пользователям с ролью USER")
    public CardResponseDTO createCard() {
        return cardService.createCard();
    }

    @PatchMapping("/{cardId}/status")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Обновить статус карты", description = "Доступно ADMIN и USER")
    public CardResponseDTO updateStatus(@PathVariable("cardId") Long cardId,
                                        @RequestParam("status") String status) {
        return cardService.updateStatus(cardId, status);
    }

    @DeleteMapping("/{cardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Удалить карту", description = "Доступно ADMIN и USER")
    public void deleteCard(@PathVariable("cardId") Long cardId) {
        cardService.deleteCard(cardId);
    }

    @GetMapping("/{cardId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Получить карту по ID", description = "Доступно ADMIN и USER")
    public CardResponseDTO getCardById(@PathVariable("cardId") Long cardId) {
        return cardService.getCardById(cardId);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить все карты", description = "Только для ADMIN, с фильтрацией по статусу")
    public Page<CardResponseDTO> getAllCardsForAdmin(
            @ParameterObject Pageable pageable,
            @RequestParam(name = "status", required = false) CardStatus status) {
        return (status != null) ?
                cardService.getAllCardsForAdmin(status, pageable) :
                cardService.getAllCardsForAdmin(pageable);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Получить мои карты", description = "Только для USER, с фильтрацией по статусу")
    public Page<CardResponseDTO> getMyCards(
            @ParameterObject Pageable pageable,
            @RequestParam(name = "status", required = false) CardStatus status) {
        return (status != null) ?
                cardService.getMyCards(status, pageable) :
                cardService.getMyCards(pageable);
    }
}

