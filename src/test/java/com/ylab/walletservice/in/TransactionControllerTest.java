package com.ylab.walletservice.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import com.ylab.walletservice.presentation.in.controller.TransactionController;
import com.ylab.walletservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Transaction controller tests")
public class TransactionControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionServiceMock;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Get history-valid player")
    public void testGetHistoryValidPlayer() throws Exception {
        LoggedInPlayerDto loggedInPlayerDto = new LoggedInPlayerDto(1L, "playerLogin");
        List<Transaction> transactionHistory = Arrays.asList(
                new Transaction(1L, 123L, 1L, new BigDecimal("100.50"), "deposit"),
                new Transaction(2L, 124L, 1L, new BigDecimal("50.75"), "withdrawal"));

        when(transactionServiceMock.getHistory(loggedInPlayerDto.id())).thenReturn(transactionHistory);

        mockMvc.perform(get("/api/transactions/history")
                        .requestAttr("player", loggedInPlayerDto))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactionHistory)));

        verify(transactionServiceMock, times(1)).getHistory(loggedInPlayerDto.id());
    }

    @Test
    @DisplayName("Get history-exception")
    public void testGetHistoryException() throws Exception {
        LoggedInPlayerDto loggedInPlayerDto = new LoggedInPlayerDto(1L, "playerLogin");

        when(transactionServiceMock.getHistory(loggedInPlayerDto.id())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/transactions/history")
                        .requestAttr("player", loggedInPlayerDto))
                .andExpect(status().isInternalServerError());

        verify(transactionServiceMock, times(1)).getHistory(loggedInPlayerDto.id());
    }

    @Test
    @DisplayName("Create transaction-valid data")
    public void testCreateTransactionValidData() throws Exception {
        LoggedInPlayerDto loggedInPlayerDto = new LoggedInPlayerDto(1L, "playerLogin");
        TransactionRequestDto transactionRequestDto =
                new TransactionRequestDto(123L, loggedInPlayerDto.id(), new BigDecimal("50.25"), "credit");

        when(transactionServiceMock.create(transactionRequestDto)).thenReturn(true);

        mockMvc.perform(post("/api/transactions/create")
                        .contentType("application/json")
                        .requestAttr("player", loggedInPlayerDto)
                        .content(objectMapper.writeValueAsString(transactionRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Transaction created successfully!"));

        verify(transactionServiceMock, times(1)).create(transactionRequestDto);
    }

    @Test
    @DisplayName("Create transaction-existing data")
    public void testCreateTransactionExistingData() throws Exception {
        LoggedInPlayerDto loggedInPlayerDto = new LoggedInPlayerDto(1L, "playerLogin");
        TransactionRequestDto transactionRequestDto =
                new TransactionRequestDto(123L, loggedInPlayerDto.id(), new BigDecimal("200.75"), "credit");

        when(transactionServiceMock.create(transactionRequestDto)).thenReturn(false);

        mockMvc.perform(post("/api/transactions/create")
                        .contentType("application/json")
                        .requestAttr("player", loggedInPlayerDto)
                        .content(objectMapper.writeValueAsString(transactionRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Transaction creation failed!"));

        verify(transactionServiceMock, times(1)).create(transactionRequestDto);
    }

    @Test
    @DisplayName("Create transaction-exception")
    public void testCreateTransactionException() throws Exception {
        LoggedInPlayerDto loggedInPlayerDto = new LoggedInPlayerDto(1L, "playerLogin");
        TransactionRequestDto transactionRequestDto =
                new TransactionRequestDto(123L, loggedInPlayerDto.id(), new BigDecimal("100.00"), "credit");

        when(transactionServiceMock.create(transactionRequestDto)).thenThrow(new RuntimeException());

        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        mockMvc.perform(post("/api/transactions/create")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transactionRequestDto))
                        .requestAttr("player", loggedInPlayerDto))
                .andExpect(status().isInternalServerError());

        verify(transactionServiceMock, times(1)).create(transactionRequestDto);
    }
}