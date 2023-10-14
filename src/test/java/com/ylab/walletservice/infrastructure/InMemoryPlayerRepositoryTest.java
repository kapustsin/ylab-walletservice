package com.ylab.walletservice.infrastructure;

import com.ylab.walletservice.domain.Player;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InMemoryPlayerRepositoryTest {

    private InMemoryPlayerRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryPlayerRepository();
    }

    @Test
    public void testGet() {
        String login = "testUser";
        Player player = new Player(1, login, "password");
        repository.create(player);

        Player retrievedPlayer = repository.get(login);
        assertNotNull(retrievedPlayer);
        assertEquals(player, retrievedPlayer);
    }

    @Test
    public void testExists() {
        String login = "existingUser";
        repository.create(new Player(1, login, "password"));

        assertTrue(repository.exists(login));
        assertFalse(repository.exists("nonExistingUser"));
    }

    @Test
    public void testCreate() {
        String login = "newUser";
        String password = "password";

        long id = repository.create(new Player(1, login, password));
        assertTrue(id > 0);
        assertTrue(repository.exists(login));
        Player createdPlayer = repository.get(login);
        assertNotNull(createdPlayer);
        assertEquals(login, createdPlayer.getLogin());
        assertEquals(password, createdPlayer.getPassword());
    }
}