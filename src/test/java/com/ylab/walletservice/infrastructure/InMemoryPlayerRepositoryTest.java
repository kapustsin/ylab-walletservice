package com.ylab.walletservice.infrastructure;

import com.ylab.walletservice.domain.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InMemoryPlayerRepositoryTest {

    private InMemoryPlayerRepository repository;

    @Before
    public void setUp() {
        repository = new InMemoryPlayerRepository();
    }

    @Test
    public void testGet() {
        String login = "testUser";
        Player player = new Player(1, login, "password");
        repository.create(login, "password");

        Player retrievedPlayer = repository.get(login);
        assertNotNull(retrievedPlayer);
        assertEquals(player, retrievedPlayer);
    }

    @Test
    public void testExists() {
        String login = "existingUser";
        repository.create(login, "password");

        assertTrue(repository.exists(login));
        assertFalse(repository.exists("nonExistingUser"));
    }

    @Test
    public void testCreate() {
        String login = "newUser";
        String password = "password";

        long id = repository.create(login, password);
        assertTrue(id > 0);
        assertTrue(repository.exists(login));
        Player createdPlayer = repository.get(login);
        System.out.println(createdPlayer);
        assertNotNull(createdPlayer);
        assertEquals(login, createdPlayer.getLogin());
        assertEquals(password, createdPlayer.getPassword());
    }
}