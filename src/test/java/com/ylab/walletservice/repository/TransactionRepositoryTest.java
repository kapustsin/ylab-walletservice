package com.ylab.walletservice.repository;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.repository.impl.JdbcTransactionRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class TransactionRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    private JdbcTransactionRepository transactionRepository;

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        System.setProperty("TEST_DB_URL", jdbcUrl);
        System.setProperty("TEST_DB_USERNAME", username);
        System.setProperty("TEST_DB_PASSWORD", password);
    }

    @AfterAll
    public static void tearDown() {
        postgresContainer.stop();
    }

    @BeforeEach
    public void initializeRepository() {
        transactionRepository = new JdbcTransactionRepository();
    }

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction(12345, 1, 100L, BigDecimal.valueOf(50.0), "credit");
        long transactionId = transactionRepository.create(transaction);
        assertTrue(transactionId > 0);
    }

    @Test
    public void testGetTransactionById() {
        Transaction transaction = new Transaction(12345, 1, 100L, BigDecimal.valueOf(50.0), "credit");
        long transactionId = transactionRepository.create(transaction);

        Optional<Transaction> retrievedTransaction = transactionRepository.get(transactionId);
        assertTrue(retrievedTransaction.isPresent());
        assertEquals(transactionId, retrievedTransaction.get().getId());
    }

    @Test
    public void testIsTransactionTokenUnique() {
        long uniqueToken = 9999999L;
        assertTrue(transactionRepository.isTransactionTokenUnique(uniqueToken));

        Transaction transaction = new Transaction(2, 344, 100L, BigDecimal.valueOf(50.0), "credit");
        transactionRepository.create(transaction);

        assertFalse(transactionRepository.isTransactionTokenUnique(344));
    }

    @Test
    public void testGetTransactionHistory() {
        long playerId = 100L;
        Transaction transaction1 = new Transaction(111, playerId, 1L, BigDecimal.valueOf(50.0), "credit");
        Transaction transaction2 = new Transaction(222, playerId, 1L, BigDecimal.valueOf(30.0), "debit");

        transactionRepository.create(transaction1);
        transactionRepository.create(transaction2);

        List<Transaction> transactions = transactionRepository.getHistory(playerId);
        assertEquals(2, transactions.size());
    }
}