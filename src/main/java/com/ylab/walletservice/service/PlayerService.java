package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.repository.PlayerRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Objects.isNull;

/**
 * Service class for managing player operations such as registration, authorization, and balance retrieval.
 */
public class PlayerService {
    private final PlayerRepository playerRepository;

    /**
     * Constructs a PlayerService object with the specified PlayerRepository instance.
     *
     * @param playerRepository The repository used for player-related data access.
     */
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Registers a new player with the given login and password.
     *
     * @param login    The login of the new player.
     * @param password The password of the new player.
     * @return true if registration is successful, false otherwise.
     */
    public boolean create(String login, String password) {
        if (!playerRepository.exists(login)) {
            Player player = new Player(generatePlayerId(), login, password);
            playerRepository.create(player);
            LogService.add("New user creation. Success.");
            return true;
        } else {
            LogService.add("New user creation. Error.");
            return false;
        }
    }

    /**
     * Authorizes a player with the given username and password.
     *
     * @param username The username of the player.
     * @param password The password of the player.
     * @return An Optional containing the authorized Player object if successful, or empty otherwise.
     */
    public Optional<Player> doAuthorisation(String username, String password) {
        Player player = playerRepository.get(username);
        if (!isNull(player) && (player.getPassword().equals(password))) {
            LogService.add("User with login " + username + "success authorization.");
            return Optional.of(player);
        } else {
            LogService.add("User with login " + username + " authorization failed.");
            return Optional.empty();
        }
    }

    /**
     * Retrieves the balance of the player with the given login.
     *
     * @param login The login of the player.
     * @return The balance of the player.
     */
    public BigDecimal getBalance(String login) {
        return playerRepository.get(login).getBalance();
    }

    /**
     * Generates a unique player ID for new transactions.
     *
     * @return The generated unique player ID.
     */
    private long generatePlayerId() {
        LogService.add("New player id generated.");
        return playerRepository.getPlayersSize() + 1;
    }
}