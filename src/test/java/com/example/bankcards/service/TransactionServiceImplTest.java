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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserService userService;
    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User user;
    private Card fromCard;
    private Card toCard;
    private TransactionRequestDTO request;
    private Transaction transaction;
    private TransactionResponseDTO response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        fromCard = new Card();
        fromCard.setId(4L);
        fromCard.setBalance(1000.0);
        fromCard.setUser(user);

        toCard = new Card();
        toCard.setId(5L);
        toCard.setBalance(500.0);

        request = new TransactionRequestDTO();
        request.setFromCardId(4L);
        request.setToCardId(5L);
        request.setAmount(200.0);

        transaction = new Transaction();
        response = new TransactionResponseDTO();
    }

    @Test
    void transfer_success() {
        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(4L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(5L)).thenReturn(Optional.of(toCard));
        when(transactionMapper.toEntity(request)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toDto(transaction)).thenReturn(response);

        TransactionResponseDTO result = transactionService.transfer(request);

        assertNotNull(result);
        assertEquals(800, fromCard.getBalance());
        assertEquals(700, toCard.getBalance());

        verify(cardRepository).save(fromCard);
        verify(cardRepository).save(toCard);
    }

    @Test
    void transfer_cardNotFound() {
        when(cardRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> transactionService.transfer(request));
    }

    @Test
    void transfer_accessDenied() {
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setFromCardId(1L);  // Эта карта принадлежит другому пользователю
        request.setToCardId(2L);
        request.setAmount(100.0);

        User anotherUser = new User();
        anotherUser.setId(99L); // другой пользователь

        Card fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setUser(anotherUser);
        fromCard.setBalance(500.0);

        Card toCard = new Card();
        toCard.setId(2L);
        toCard.setUser(user);
        toCard.setBalance(500.0);

        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        assertThrows(AccessDeniedException.class, () -> transactionService.transfer(request));
    }


    @Test
    void transfer_insufficientFunds() {
        request.setAmount(2000.0);
        when(userService.getCurrentUser()).thenReturn(user);
        when(cardRepository.findById(4L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(5L)).thenReturn(Optional.of(toCard));

        assertThrows(InsufficientBalanceException.class, () -> transactionService.transfer(request));
    }
}
