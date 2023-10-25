package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Disabled
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

   /* @Test
    void testCreateDebitTransactionWithValidAmount() {
        Player player = new Player(1, "testuser", "password", BigDecimal.valueOf(100));
        BigDecimal debitAmount = BigDecimal.valueOf(50);

        when(transactionRepository.isTransactionTokenUnique(anyLong())).thenReturn(true);
        when(playerService.getBalance(eq("testuser"))).thenReturn(player.getBalance());

        transactionService.create(player, debitAmount, "debit");

        verify(transactionRepository, times(1)).create(any(Transaction.class));
        verify(playerService, times(1)).setBalance(eq(1L), eq(BigDecimal.valueOf(50)));
    }

    @Test
    void testCreateDebitTransactionWithInvalidAmount() {
        Player player = new Player(1, "testuser", "password", BigDecimal.valueOf(100));
        BigDecimal debitAmount = BigDecimal.valueOf(150);

        when(playerService.getBalance(eq("testuser"))).thenReturn(player.getBalance());

        transactionService.create(player, debitAmount, "debit");

        verify(transactionRepository, never()).create(any(Transaction.class));
        verify(playerService, never()).setBalance(anyLong(), any(BigDecimal.class));
    }

    @Test
    void testCreateCreditTransaction() {
        Player player = new Player(1, "testuser", "password", BigDecimal.valueOf(100));
        BigDecimal creditAmount = BigDecimal.valueOf(50);

        when(transactionRepository.isTransactionTokenUnique(anyLong())).thenReturn(true);
        when(playerService.getBalance(eq("testuser"))).thenReturn(player.getBalance());

        transactionService.create(player, creditAmount, "credit");

        verify(transactionRepository, times(1)).create(any(Transaction.class));
        verify(playerService, times(1)).setBalance(eq(1L), eq(BigDecimal.valueOf(150)));
    }

    @Test
    void testCreateNonUniqueTransactionToken() {
        Player player = new Player(1, "testuser", "password", BigDecimal.valueOf(100));
        BigDecimal creditAmount = BigDecimal.valueOf(50);

        when(transactionRepository.isTransactionTokenUnique(anyLong())).thenReturn(false);

        transactionService.create(player, creditAmount, "credit");

        verify(transactionRepository, never()).create(any(Transaction.class));
        verify(playerService, never()).setBalance(anyLong(), any(BigDecimal.class));
    }*/
}