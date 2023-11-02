package com.ylab.walletservice.repository;

import com.ylab.walletservice.configuration.ApplicationTest;
import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.repository.impl.JdbcTransactionRepository;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationTest.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Transaction repository tests")
public class TransactionRepositoryTest {
    private static final String SCHEMA_LIQUIBASE = "liquibase";
    private static final String SCHEMA_ENTITY = "walletservice";
    private static final String CHANGELOG_PATH = "db/changelog/changelog.xml";
    private static final String ENV_NAME = "TEST_CONTAINER";
    private static final String CONTAINER = "postgres:15";
    private static final String DB_USER = "test";
    private static final String DB_PASSWORD = "test";

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(CONTAINER)
            .withDatabaseName(SCHEMA_ENTITY)
            .withUsername(DB_USER)
            .withPassword(DB_PASSWORD);
    private final JdbcTransactionRepository transactionRepository;
    private final DataSource dataSource;

    @Autowired
    public TransactionRepositoryTest(JdbcTransactionRepository transactionRepository, DataSource dataSource) {
        this.transactionRepository = transactionRepository;
        this.dataSource = dataSource;
    }

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
        System.setProperty("CUSTOM_ENV", ENV_NAME);
        System.setProperty("TEST_CONTAINER_URL", postgresContainer.getJdbcUrl());
        System.setProperty("TEST_CONTAINER_USERNAME", postgresContainer.getUsername());
        System.setProperty("TEST_CONTAINER_PASSWORD", postgresContainer.getPassword());
        System.setProperty("TEST_CONTAINER_DRIVER", postgresContainer.getDriverClassName());
    }

    @AfterAll
    public static void shutDown() {
        postgresContainer.stop();
    }

    @BeforeEach
    public void initializeRepository() {
        try (Connection connection = dataSource.getConnection()) {
            connection.createStatement().executeUpdate("CREATE SCHEMA " + SCHEMA_LIQUIBASE);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                    new JdbcConnection(connection));
            database.setLiquibaseSchemaName(SCHEMA_LIQUIBASE);
            Liquibase liquibase = new Liquibase(CHANGELOG_PATH, new ClassLoaderResourceAccessor(),
                    database);
            liquibase.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Statement statement = dataSource.getConnection().createStatement()) {
            statement.executeUpdate("DROP SCHEMA " + SCHEMA_LIQUIBASE + " CASCADE");
            statement.executeUpdate("DROP SCHEMA " + SCHEMA_ENTITY + " CASCADE");
        }
    }

    @Test
    @DisplayName("Get transaction by ID")
    public void testGetTransactionById() {
        Transaction transaction = new Transaction(12345, 1, 100L, BigDecimal.valueOf(50.0), "credit");
        long transactionId = transactionRepository.create(transaction);

        Optional<Transaction> retrievedTransaction = transactionRepository.get(transactionId);
        assertTrue(retrievedTransaction.isPresent());
        assertEquals(transactionId, retrievedTransaction.get().getId());
    }

    @Test
    @DisplayName("Get non-existing transaction")
    public void testGetNonExistingTransaction() {
        long nonExistingTransactionId = 999999L;
        Optional<Transaction> retrievedTransaction = transactionRepository.get(nonExistingTransactionId);
        assertFalse(retrievedTransaction.isPresent());
    }

    @Test
    @DisplayName("Create transaction")
    public void testCreateTransaction() {
        Transaction transaction = new Transaction(12345, 1, 100L, BigDecimal.valueOf(50.0), "credit");
        long transactionId = transactionRepository.create(transaction);
        assertTrue(transactionId > 0);
    }

    @Test
    @DisplayName("Check unique transaction token")
    public void testIsTransactionTokenUnique() {
        long uniqueToken = 9999999L;
        assertTrue(transactionRepository.isTransactionTokenUnique(uniqueToken));

        Transaction transaction = new Transaction(2, 344, 100L, BigDecimal.valueOf(50.0), "credit");
        transactionRepository.create(transaction);

        assertFalse(transactionRepository.isTransactionTokenUnique(344));
    }

    @Test
    @DisplayName("Check non-unique transaction token")
    public void testIsTransactionTokenNotUnique() {
        long nonUniqueToken = 345L;

        Transaction transaction1 = new Transaction(1, 345, 101L, BigDecimal.valueOf(30.0), "debit");
        Transaction transaction2 = new Transaction(2, 346, 102L, BigDecimal.valueOf(40.0), "credit");

        transactionRepository.create(transaction1);
        transactionRepository.create(transaction2);

        assertFalse(transactionRepository.isTransactionTokenUnique(nonUniqueToken));
    }

    @Test
    @DisplayName("Get transaction history")
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
    @DisplayName("Get transaction history for player without transactions")
    public void testGetTransactionHistoryForPlayerWithoutTransactions() {
        long playerId = 9L;

        List<Transaction> transactions = transactionRepository.getHistory(playerId);
        assertTrue(transactions.isEmpty());
    }
}