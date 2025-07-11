package com.example.bankcards.service;

import com.example.bankcards.dto.CardMapper;
import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InvalidCardStatusException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.service.impl.CardServiceImpl;
import com.example.bankcards.util.EncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserService userService;
    @Mock
    private EncryptionUtils encryptionUtils;

    @InjectMocks
    private CardServiceImpl cardService;

    private User testUser;
    private Card card;
    private CardResponseDTO responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setFirstName("Иван");
        testUser.setLastName("Иванов");

        card = new Card();
        card.setId(1L);
        card.setStatus(CardStatus.ACTIVE);
        card.setUser(testUser);
        card.setNumber("1234567890123456"); // <== вот это ключевая строка

        responseDto = new CardResponseDTO();
        responseDto.setId(1L);
    }

    @Test
    void createCard_success() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(encryptionUtils.encrypt(anyString())).thenReturn("encrypted-card-number");
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardMapper.toDto(any(Card.class))).thenReturn(responseDto);

        CardResponseDTO result = cardService.createCard();

        assertEquals(1L, result.getId());
        verify(encryptionUtils, times(1)).encrypt(anyString());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_userNotFound() {
        when(userService.getCurrentUser()).thenReturn(null); //

        assertThrows(NullPointerException.class, () -> cardService.createCard());
    }

    @Test
    void updateStatus_success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        when(cardMapper.toDto(any(Card.class))).thenReturn(responseDto);

        CardResponseDTO result = cardService.updateStatus(1L, "BLOCKED");
        assertEquals(1L, result.getId());
    }

    @Test
    void updateStatus_invalidStatus() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        assertThrows(InvalidCardStatusException.class, () -> cardService.updateStatus(1L, "INVALID"));
    }

    @Test
    void deleteCard_success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        cardService.deleteCard(1L);
        verify(cardRepository, times(1)).delete(card);
    }

    @Test
    void deleteCard_notFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.deleteCard(1L));
    }

    @Test
    void getCardById_success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardMapper.toDto(card)).thenReturn(responseDto);
        CardResponseDTO result = cardService.getCardById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCardById_notFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(1L));
    }

    @Test
    void getMyCards_success() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(cardRepository.findAllByUser(eq(testUser), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(card)));
        when(cardMapper.toDto(card)).thenReturn(responseDto);

        Page<CardResponseDTO> page = cardService.getMyCards(Pageable.ofSize(10));
        assertEquals(1, page.getContent().size());
    }

    @Test
    void getMyCards_withStatus_success() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(cardRepository.findAllByUserAndStatus(eq(testUser), eq(CardStatus.ACTIVE), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(card)));
        when(cardMapper.toDto(card)).thenReturn(responseDto);

        Page<CardResponseDTO> page = cardService.getMyCards(CardStatus.ACTIVE, Pageable.ofSize(10));
        assertEquals(1, page.getContent().size());
    }
}
