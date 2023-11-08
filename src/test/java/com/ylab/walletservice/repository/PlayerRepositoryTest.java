package com.ylab.walletservice.repository;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.repository.impl.JdbcPlayerRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@Testcontainers
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("Player repository tests")
public class PlayerRepositoryTest {
    private static final String SCHEMA_ENTITY = "walletservice";
    private static final String CONTAINER = "postgres:15";
    private static final String DB_USER = "test";
    private static final String DB_PASSWORD = "test";
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(CONTAINER)
            .withDatabaseName(SCHEMA_ENTITY)
            .withUsername(DB_USER)
            .withPassword(DB_PASSWORD);

    private final JdbcPlayerRepository playerRepository;

    @Autowired
    public PlayerRepositoryTest(JdbcPlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @BeforeAll
    public static void setUp() {
        postgresContainer.start();
        System.setProperty("TEST_CONTAINER_URL", postgresContainer.getJdbcUrl());
        System.setProperty("TEST_CONTAINER_USERNAME", postgresContainer.getUsername());
        System.setProperty("TEST_CONTAINER_PASSWORD", postgresContainer.getPassword());
    }

    @AfterAll
    public static void shutDown() {
        postgresContainer.stop();
    }

    @Test
    @DisplayName("It should create a player successfully")
    public void testCreatePlayer() {
        Player player = new Player("testuser1", "password");
        assertTrue(playerRepository.create(player) > 0);
    }

    @Test
    @DisplayName("It should retrieve an existing player successfully")
    public void testGetPlayer() {
        Player player = new Player("testuser2", "password");
        long playerId = playerRepository.create(player);

        Optional<Player> retrievedPlayer = playerRepository.get("testuser2");
        assertTrue(retrievedPlayer.isPresent());
        assertEquals(playerId, retrievedPlayer.get().getId());
    }

    @Test
    @DisplayName("It should handle retrieval of a non-existing player")
    public void testGetNonExistingPlayer() {
        Optional<Player> retrievedPlayer = playerRepository.get("nonExistingUser");
        assertFalse(retrievedPlayer.isPresent());
    }

    @Test
    @DisplayName("It should update player's balance successfully")
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