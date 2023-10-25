package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Disabled
public class PlayerServiceTest {
    private PlayerRepository playerRepository;
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        playerRepository = mock(PlayerRepository.class);
        playerService = new PlayerService(playerRepository);
    }

/*    @Test
    public void testCreateNewUserSuccess() {
        String login = "newUser";
        String password = "password123";
        Optional<Player> emptyPlayer = Optional.empty();

        when(playerRepository.get(login)).thenReturn(emptyPlayer);
        when(playerRepository.create(any(Player.class))).thenReturn(1L);

        boolean result = playerService.create(login, password);

        assertTrue(result);

        Player expectedPlayer = new Player(login, password);
        verify(playerRepository, times(1)).create(expectedPlayer);
        verify(playerRepository, times(1)).get(login);
    }*/

/*    @Test
    public void testCreateExistingUser() {
        String existingUserLogin = "existingUser";
        when(playerRepository.get(existingUserLogin)).thenReturn(
                Optional.of(new Player(1, existingUserLogin, "password123")));

        boolean result = playerService.create(existingUserLogin, "password123");

        assertFalse(result);
        verify(playerRepository, never()).create(any(Player.class));
    }*/

/*    @Test
    public void testDoAuthorizationSuccess() {
        String userLogin = "user123";
        Player existingPlayer = new Player(1, userLogin, "password123");
        when(playerRepository.get(userLogin)).thenReturn(Optional.of(existingPlayer));

        Optional<Player> result = playerService.doAuthorisation(userLogin, "password123");

        assertTrue(result.isPresent());
        assertEquals(existingPlayer, result.get());
    }*/

/*    @Test
    public void testDoAuthorizationFailure() {
        when(playerRepository.get("nonExistingUser")).thenReturn(Optional.empty());

        Optional<Player> result = playerService.doAuthorisation("nonExistingUser", "wrongPassword");

        assertFalse(result.isPresent());
    }*/

    @Test
    public void testGetBalance() {
        String userLogin = "user123";
        BigDecimal expectedBalance = new BigDecimal("1000.0");

        Player player = new Player(1, userLogin, "password123");
        player.setBalance(expectedBalance);

        when(playerRepository.get(userLogin)).thenReturn(Optional.of(player));

        BigDecimal balance = playerService.getBalance(1);

        assertEquals(expectedBalance, balance);
    }
}