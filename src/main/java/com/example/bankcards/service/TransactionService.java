package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionRequestDTO;
import com.example.bankcards.dto.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponseDTO transfer(TransactionRequestDTO request);
    Page<TransactionResponseDTO> getTransactionHistory(Pageable pageable);
}
