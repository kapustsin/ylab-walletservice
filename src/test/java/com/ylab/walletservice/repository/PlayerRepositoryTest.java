package com.ylab.walletservice.repository;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.repository.impl.JdbcPlayerRepository;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class PlayerRepositoryTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    private JdbcPlayerRepository playerRepository;

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

    @AfterEach
    public void tearDown() {
        postgresContainer.stop();
    }

    @BeforeEach
    public void initializeRepository() {
        playerRepository = new JdbcPlayerRepository();
    }

    @Test
    public void testCreatePlayer() {
        Player player = new Player("testuser1", "password");
        long playerId = playerRepository.create(player);
        assertTrue(playerId > 0);
    }

    @Test
    public void testGetPlayer() {
        Player player = new Player("testuser2", "password");
        long playerId = playerRepository.create(player);
        System.out.println(playerId);

        Optional<Player> retrievedPlayer = playerRepository.get("testuser2");
        assertTrue(retrievedPlayer.isPresent());
        assertEquals(playerId, retrievedPlayer.get().getId());
    }

    @Test
    public void testUpdateBalance() {
        Player player = new Player("testuser3", "password");
        long playerId = playerRepository.create(player);

        BigDecimal newBalance = new BigDecimal("100.50");
        playerRepository.updateBalance(playerId, newBalance);

        Optional<Player> updatedPlayer = playerRepository.get("testuser3");
        assertTrue(updatedPlayer.isPresent());
        assertEquals(newBalance, updatedPlayer.get().getBalance());
    }

}