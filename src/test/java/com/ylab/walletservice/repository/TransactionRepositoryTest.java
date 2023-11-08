package com.ylab.walletservice.repository;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.repository.impl.JdbcTransactionRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@Testcontainers
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Transaction repository tests")
public class TransactionRepositoryTest {
    private static final String SCHEMA_ENTITY = "walletservice";
    private static final String CONTAINER = "postgres:15";
    private static final String DB_USER = "test";
    private static final String DB_PASSWORD = "test";
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(CONTAINER)
            .withDatabaseName(SCHEMA_ENTITY)
            .withUsername(DB_USER)
            .withPassword(DB_PASSWORD);

    private final JdbcTransactionRepository transactionRepository;

    @Autowired
    public TransactionRepositoryTest(JdbcTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
        System.setProperty("TEST_CONTAINER_URL", postgresContainer.getJdbcUrl());
        System.setProperty("TEST_CONTAINER_USERNAME", postgresContainer.getUsername());
        System.setProperty("TEST_CONTAINER_PASSWORD", postgresContainer.getPassword());
        System.setProperty("TEST_CONTAINER_DRIVER", postgresContainer.getDriverClassName());
    }

    @AfterAll
    public static void shutDown() {
        postgresContainer.stop();
    }

    @Test
    @DisplayName("It should retrieve transaction by ID successfully")
    public void testGetTransactionById() {
        Transaction transaction = new Transaction(12345, 1, 100L, BigDecimal.valueOf(50.0), "credit");
        long transactionId = transactionRepository.create(transaction);

        Optional<Transaction> retrievedTransaction = transactionRepository.get(transactionId);
        assertTrue(retrievedTransaction.isPresent());
        assertEquals(transactionId, retrievedTransaction.get().getId());
    }

    @Test
    @DisplayName("It should handle retrieval of a non-existing transaction")
    public void testGetNonExistingTransaction() {
        long nonExistingTransactionId = 999999L;
        Optional<Transaction> retrievedTransaction = transactionRepository.get(nonExistingTransactionId);
        assertFalse(retrievedTransaction.isPresent());
    }

    @Test
    @DisplayName("It should create transaction successfully")
    public void testCreateTransaction() {
        Transaction transaction = new Transaction(12345, 1, 100L, BigDecimal.valueOf(50.0), "credit");
        long transactionId = transactionRepository.create(transaction);
        assertTrue(transactionId > 0);
    }

    @Test
    @DisplayName("It should check uniqueness of transaction token")
    public void testIsTransactionTokenUnique() {
        long uniqueToken = 9999999L;
        assertTrue(transactionRepository.isTransactionTokenUnique(uniqueToken));

        Transaction transaction = new Transaction(2, 344, 100L, BigDecimal.valueOf(50.0), "credit");
        transactionRepository.create(transaction);

        assertFalse(transactionRepository.isTransactionTokenUnique(344));
    }

    @Test
    @DisplayName("It should handle non-uniqueness of transaction token")
    public void testIsTransactionTokenNotUnique() {
        long nonUniqueToken = 345L;

        Transaction transaction1 = new Transaction(1, 345, 101L, BigDecimal.valueOf(30.0), "debit");
        Transaction transaction2 = new Transaction(2, 346, 102L, BigDecimal.valueOf(40.0), "credit");

        transactionRepository.create(transaction1);
        transactionRepository.create(transaction2);

        assertFalse(transactionRepository.isTransactionTokenUnique(nonUniqueToken));
    }

    @Test
    @DisplayName("It should retrieve transaction history for player")
    public void testGetTransactionHistory() {
        long playerId = 8L;
        Transaction transaction1 = new Transaction(111, playerId, 8L, BigDecimal.valueOf(50.0), "credit");
        Transaction transaction2 = new Transaction(222, playerId, 8L, BigDecimal.valueOf(30.0), "debit");

        transactionRepository.create(transaction1);
        transactionRepository.create(transaction2);

        List<Transaction> transactions = transactionRepository.getHistory(playerId);
        assertEquals(2, transactions.size());
    }

    @Test
    @DisplayName("It should handle retrieval of transaction history for player without transactions")
    public void testGetTransactionHistoryForPlayerWithoutTransactions() {
        long playerId = 9L;

        List<Transaction> transactions = transactionRepository.getHistory(playerId);
        assertTrue(transactions.isEmpty());
    }
}