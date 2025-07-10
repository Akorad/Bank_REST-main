package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    CardResponseDTO createCard ();

    CardResponseDTO updateStatus (Long cardId,String status);

    void deleteCard (Long cardId);

    CardResponseDTO getCardById(Long id);

    Page<CardResponseDTO> getAllCardsForAdmin(Pageable pageable);

    Page<CardResponseDTO> getMyCards(Pageable pageable);

    Page<CardResponseDTO> getMyCards(CardStatus status, Pageable pageable);

    Page<CardResponseDTO> getAllCardsForAdmin(CardStatus status, Pageable pageable);
}
