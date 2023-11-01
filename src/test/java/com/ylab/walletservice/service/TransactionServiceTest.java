package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import com.ylab.walletservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Disabled
@DisplayName("Transaction service test")
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create debit transaction with valid amount")
    void testCreateDebitTransactionWithValidAmount() {
        TransactionRequestDto requestDto = new TransactionRequestDto(123, 1, BigDecimal.valueOf(50), "debit");

        when(transactionRepository.isTransactionTokenUnique(anyLong())).thenReturn(true);
        when(playerService.getBalance(eq(1L))).thenReturn(BigDecimal.valueOf(100));

        transactionService.create(requestDto);

        verify(transactionRepository, times(1)).create(any(Transaction.class));
        verify(playerService, times(1)).setBalance(eq(1L), eq(BigDecimal.valueOf(50)));
    }

    @Test
    @DisplayName("Create debit transaction with invalid amount")
    void testCreateDebitTransactionWithInvalidAmount() {
        TransactionRequestDto requestDto = new TransactionRequestDto(123, 1, BigDecimal.valueOf(150), "debit");

        when(playerService.getBalance(eq(1L))).thenReturn(BigDecimal.valueOf(100));

        transactionService.create(requestDto);

        verify(transactionRepository, never()).create(any(Transaction.class));
        verify(playerService, never()).setBalance(anyLong(), any(BigDecimal.class));
    }

    @Test
    @DisplayName("Create credit transaction")
    void testCreateCreditTransaction() {
        TransactionRequestDto requestDto = new TransactionRequestDto(123, 1, BigDecimal.valueOf(50), "credit");

        when(transactionRepository.isTransactionTokenUnique(anyLong())).thenReturn(true);
        when(playerService.getBalance(eq(1L))).thenReturn(BigDecimal.valueOf(100));

        transactionService.create(requestDto);

        verify(transactionRepository, times(1)).create(any(Transaction.class));
        verify(playerService, times(1)).setBalance(eq(1L), eq(BigDecimal.valueOf(150)));
    }

    @Test
    @DisplayName("Create transaction with non-unique transaction token")
    void testCreateNonUniqueTransactionToken() {
        TransactionRequestDto requestDto = new TransactionRequestDto(123, 1, BigDecimal.valueOf(50), "credit");

        when(transactionRepository.isTransactionTokenUnique(anyLong())).thenReturn(false);

        transactionService.create(requestDto);

        verify(transactionRepository, never()).create(any(Transaction.class));
        verify(playerService, never()).setBalance(anyLong(), any(BigDecimal.class));
    }

    @Test
    @DisplayName("Get transaction history")
    void testGetTransactionHistory() {
        long playerId = 1;
        List<Transaction> expectedTransactions = Arrays.asList(
                new Transaction(1, playerId, BigDecimal.TEN, "debit"),
                new Transaction(2, playerId, BigDecimal.valueOf(20), "credit")
        );

        when(transactionRepository.getHistory(playerId)).thenReturn(expectedTransactions);
        List<Transaction> actualTransactions = transactionService.getHistory(playerId);

        assertIterableEquals(expectedTransactions, actualTransactions);
    }
}