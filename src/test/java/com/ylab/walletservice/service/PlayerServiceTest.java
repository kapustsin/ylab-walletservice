package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import com.ylab.walletservice.domain.mapper.PlayerMapper;
import com.ylab.walletservice.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerServiceTest {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewUserSuccess() {
        String login = "newUser";
        String password = "password123";
        Optional<Player> emptyPlayer = Optional.empty();

        when(playerRepository.get(login)).thenReturn(emptyPlayer);
        when(playerRepository.create(any(Player.class))).thenReturn(1L);

        boolean result = playerService.create(new RegistrationDto(login, password));

        assertTrue(result);

        Player expectedPlayer = new Player(login, password);
        verify(playerRepository, times(1)).create(expectedPlayer);
        verify(playerRepository, times(1)).get(login);
    }

    @Test
    public void testCreateExistingUser() {
        String login = "existingUser";
        String password = "password123";
        when(playerRepository.get(login)).thenReturn(
                Optional.of(new Player(1, login, password)));

        boolean result = playerService.create(new RegistrationDto(login, password));

        assertFalse(result);
        verify(playerRepository, never()).create(any(Player.class));
    }

    @Test
    public void testDoAuthorizationSuccess() {
        String userLogin = "user123";
        String password = "password123";

        CredentialsDto credentials = new CredentialsDto(userLogin, password);
        Player existingPlayer = new Player(1, userLogin, password);
        LoggedInPlayerDto expectedLoggedInPlayerDto = new LoggedInPlayerDto(1, userLogin);

        when(playerRepository.get(userLogin)).thenReturn(Optional.of(existingPlayer));
        when(playerMapper.playerToLoggedInPlayerDto(existingPlayer)).thenReturn(expectedLoggedInPlayerDto);

        Optional<LoggedInPlayerDto> result = playerService.doAuthorisation(credentials);

        assertTrue(result.isPresent());
        assertEquals(expectedLoggedInPlayerDto, result.get());
    }

    @Test
    public void testDoAuthorizationFailure() {
        String nonExistingUserLogin = "nonExistingUser";
        String wrongPassword = "wrongPassword";

        CredentialsDto credentials = new CredentialsDto(nonExistingUserLogin, wrongPassword);
        when(playerRepository.get(nonExistingUserLogin)).thenReturn(Optional.empty());

        Optional<LoggedInPlayerDto> result = playerService.doAuthorisation(credentials);

        assertFalse(result.isPresent());
    }

    @Test
    public void testAuthorizationWithInvalidCredentials() {
        String existingUserLogin = "user123";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";

        CredentialsDto correctCredentials = new CredentialsDto(existingUserLogin, correctPassword);
        CredentialsDto wrongCredentials = new CredentialsDto(existingUserLogin, wrongPassword);

        Player existingPlayer = new Player(1, existingUserLogin, correctPassword);

        when(playerRepository.get(existingUserLogin)).thenReturn(Optional.of(existingPlayer));

        Optional<LoggedInPlayerDto> correctResult = playerService.doAuthorisation(correctCredentials);
        Optional<LoggedInPlayerDto> wrongResult = playerService.doAuthorisation(wrongCredentials);

        assertTrue(correctResult.isPresent());
        assertFalse(wrongResult.isPresent());
    }

    @Test
    void testGetBalance() {
        long playerId = 1;
        BigDecimal expectedBalance = new BigDecimal("1000.0");

        when(playerRepository.getBalance(playerId)).thenReturn(expectedBalance);

        BigDecimal balance = playerService.getBalance(playerId);

        assertEquals(expectedBalance, balance);
        verify(playerRepository, times(1)).getBalance(playerId);
    }

    @Test
    public void testGetBalanceForNonExistingUser() {
        long nonExistingUserId = 999;

        when(playerRepository.getBalance(nonExistingUserId)).thenReturn(
                null);

        BigDecimal balance = playerService.getBalance(nonExistingUserId);

        assertNull(balance);
        verify(playerRepository, times(1)).getBalance(nonExistingUserId);
    }
}