package com.example.bankcards.controller;

import com.example.bankcards.dto.TransactionRequestDTO;
import com.example.bankcards.dto.TransactionResponseDTO;
import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.security.JwtTokenProvider;
import com.example.bankcards.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TransactionController.class)
@Import(TransactionControllerTest.MockConfig.class)
@AutoConfigureMockMvc(addFilters = false) // Отключаем Spring Security для тестов
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionResponseDTO sampleResponse;
    private TransactionRequestDTO sampleRequest;


    @BeforeEach
    void setUp() {
        sampleResponse = new TransactionResponseDTO();
        sampleResponse.setId(101L);
        sampleResponse.setFromCardNumberMasked("**** **** **** 1234");
        sampleResponse.setToCardNumberMasked("**** **** **** 5678");
        sampleResponse.setAmount(1500.00);
        sampleResponse.setCreatedAt("2025-07-10T12:34:56");

        sampleRequest = new TransactionRequestDTO();
        sampleRequest.setFromCardId(4L);
        sampleRequest.setToCardId(5L);
        sampleRequest.setAmount(1500.00);

        Mockito.when(transactionService.transfer(any(TransactionRequestDTO.class)))
                .thenReturn(sampleResponse);

        Page<TransactionResponseDTO> page = new PageImpl<>(
                Collections.singletonList(sampleResponse),
                PageRequest.of(0, 10),
                1
        );

        Mockito.when(transactionService.getTransactionHistory(any(Pageable.class)))
                .thenReturn(page);
    }

    @Test
    void transfer_shouldReturnTransaction() throws Exception {
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101))
                .andExpect(jsonPath("$.fromCardNumberMasked").value("**** **** **** 1234"))
                .andExpect(jsonPath("$.toCardNumberMasked").value("**** **** **** 5678"))
                .andExpect(jsonPath("$.amount").value(1500.00))
                .andExpect(jsonPath("$.createdAt").value("2025-07-10T12:34:56"));
    }

    @Test
    void getHistory_shouldReturnPagedTransactions() throws Exception {
        mockMvc.perform(get("/api/v1/transactions/history")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(101))
                .andExpect(jsonPath("$.content[0].fromCardNumberMasked").value("**** **** **** 1234"))
                .andExpect(jsonPath("$.content[0].toCardNumberMasked").value("**** **** **** 5678"))
                .andExpect(jsonPath("$.content[0].amount").value(1500.00))
                .andExpect(jsonPath("$.content[0].createdAt").value("2025-07-10T12:34:56"));
    }

    @TestConfiguration
    static class MockConfig {

        @Bean
        public TransactionService transactionService() {
            return Mockito.mock(TransactionService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            return Mockito.mock(JwtTokenProvider.class);
        }

        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
            return Mockito.mock(JwtAuthenticationFilter.class);
        }
    }
}
