package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionMapper;
import com.example.bankcards.dto.TransactionRequestDTO;
import com.example.bankcards.dto.TransactionResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientBalanceException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponseDTO transfer(TransactionRequestDTO request) {
        User currentUser = userService.getCurrentUser();

        Card fromCard = cardRepository.findById(request.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException(request.getFromCardId()));

        Card toCard = cardRepository.findById(request.getToCardId())
                .orElseThrow(() -> new CardNotFoundException(request.getToCardId()));

        if (!fromCard.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Вы не являетесь владельцем карты отправителя");
        }

        if (fromCard.getBalance() < request.getAmount()) {
            throw new InsufficientBalanceException(fromCard.getNumber());
        }

        fromCard.setBalance(fromCard.getBalance() - request.getAmount());
        toCard.setBalance(toCard.getBalance() + request.getAmount());

        cardRepository.save(fromCard);

        cardRepository.save(toCard);

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setFromCard(fromCard);
        transaction.setToCard(toCard);
        transaction.setCreatedAt(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toDto(savedTransaction);
    }

    @Override
    public Page<TransactionResponseDTO> getTransactionHistory(Pageable pageable) {
        User currentUser = userService.getCurrentUser();

        Page<Transaction> transactions = transactionRepository.findAllByFromCardUserIdOrToCardUserId(currentUser.getId(), currentUser.getId(), pageable);

        return transactions.map(transactionMapper::toDto);
    }
}
