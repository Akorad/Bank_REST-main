package com.example.bankcards.controller;

import com.example.bankcards.dto.CardResponseDTO;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.security.JwtTokenProvider;
import com.example.bankcards.security.UserDetailsServiceImpl;
import com.example.bankcards.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(CardController.class)
@Import(CardControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false) // отключаем Spring Security
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardService cardService;

    private CardResponseDTO sampleCard;

    @BeforeEach
    void setUp() {
        sampleCard = new CardResponseDTO();
        sampleCard.setId(1L);
        sampleCard.setNumber("**** **** **** 1234");
        sampleCard.setOwnerName("IVAN IVANOV");
        sampleCard.setExpirationDate("12/25");
        sampleCard.setStatus(CardStatus.ACTIVE);

        Mockito.when(cardService.createCard()).thenReturn(sampleCard);
        Mockito.when(cardService.getCardById(1L)).thenReturn(sampleCard);
        Mockito.when(cardService.updateStatus(1L, "ACTIVE")).thenReturn(sampleCard);

        Page<CardResponseDTO> page = new PageImpl<>(
                java.util.Collections.singletonList(sampleCard),
                PageRequest.of(0, 10),
                1
        );
        Mockito.when(cardService.getMyCards(any(Pageable.class))).thenReturn(page);
        Mockito.when(cardService.getAllCardsForAdmin(any(Pageable.class))).thenReturn(page);
    }

    @Test
    void createCard_shouldReturnCard() throws Exception {
        mockMvc.perform(post("/api/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.number").value("**** **** **** 1234"));
    }

    @Test
    void getCardById_shouldReturnCard() throws Exception {
        mockMvc.perform(get("/api/v1/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateStatus_shouldUpdateCard() throws Exception {
        mockMvc.perform(patch("/api/v1/cards/1/status")
                        .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void deleteCard_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/cards/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getMyCards_shouldReturnPagedCards() throws Exception {
        mockMvc.perform(get("/api/v1/cards/my")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void getAllCardsForAdmin_shouldReturnPagedCards() throws Exception {
        mockMvc.perform(get("/api/v1/cards/admin")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @TestConfiguration
    static class MockConfig {


        @Bean
        public CardService cardService() {
            return Mockito.mock(CardService.class);
        }

        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return Mockito.mock(JwtTokenProvider.class);
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter(
                JwtTokenProvider tokenProvider) {
            return Mockito.mock(JwtAuthenticationFilter.class);
        }

        @Bean
        public UserDetailsServiceImpl userDetailsService() {
            return Mockito.mock(UserDetailsServiceImpl.class);
        }
    }
}
