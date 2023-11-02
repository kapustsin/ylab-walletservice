package com.ylab.walletservice.repository;

import com.ylab.walletservice.configuration.ApplicationTest;
import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.repository.impl.JdbcPlayerRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationTest.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Player repository tests")
public class PlayerRepositoryTest {
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
    private final JdbcPlayerRepository playerRepository;
    private final DataSource dataSource;

    @Autowired
    public PlayerRepositoryTest(JdbcPlayerRepository playerRepository, DataSource dataSource) {
        this.playerRepository = playerRepository;
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
    @DisplayName("Create player")
    public void testCreatePlayer() {
        Player player = new Player("testuser1", "password");
        assertTrue(playerRepository.create(player) > 0);
    }

    @Test
    @DisplayName("Get player")
    public void testGetPlayer() {
        Player player = new Player("testuser2", "password");
        long playerId = playerRepository.create(player);

        Optional<Player> retrievedPlayer = playerRepository.get("testuser2");
        assertTrue(retrievedPlayer.isPresent());
        assertEquals(playerId, retrievedPlayer.get().getId());
    }

    @Test
    @DisplayName("Get non-existing player")
    public void testGetNonExistingPlayer() {
        Optional<Player> retrievedPlayer = playerRepository.get("nonExistingUser");
        assertFalse(retrievedPlayer.isPresent());
    }

    @Test
    @DisplayName("Update balance")
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