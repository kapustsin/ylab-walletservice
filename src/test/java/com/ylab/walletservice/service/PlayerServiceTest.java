package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class PlayerServiceTest {
    private PlayerRepository playerRepository;
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        playerRepository = mock(PlayerRepository.class);
        playerService = new PlayerService(playerRepository);
    }

    @Test
    public void testCreateNewUserSuccess() {
        when(playerRepository.exists("newUser")).thenReturn(false);

        boolean result = playerService.create("newUser", "password123");

        assertTrue(result);
        verify(playerRepository, times(1)).create(new Player(1, "newUser", "password123"));
    }

    @Test
    public void testCreateExistingUser() {
        when(playerRepository.exists("existingUser")).thenReturn(true);

        boolean result = playerService.create("existingUser", "password123");

        assertFalse(result);
        verify(playerRepository, never()).create(any(Player.class));
    }

    @Test
    public void testDoAuthorizationSuccess() {
        Player existingPlayer = new Player(1, "user123", "password123");
        when(playerRepository.get("user123")).thenReturn(existingPlayer);

        Optional<Player> result = playerService.doAuthorisation("user123", "password123");

        assertTrue(result.isPresent());
        assertEquals(existingPlayer, result.get());
    }

    @Test
    public void testDoAuthorizationFailure() {
        when(playerRepository.get("nonExistingUser")).thenReturn(null);

        Optional<Player> result = playerService.doAuthorisation("nonExistingUser", "wrongPassword");

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetBalance() {
        Player player = new Player(1, "user123", "password123");
        player.setBalance(new BigDecimal("1000.0"));
        when(playerRepository.get("user123")).thenReturn(player);

        BigDecimal balance = playerService.getBalance("user123");
        BigDecimal expectedBalance = new BigDecimal("1000.0");

        assertEquals(balance, expectedBalance);
    }
}