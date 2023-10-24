package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.repository.PlayerRepository;

import java.math.BigDecimal;
import java.util.Optional;

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
        if (playerRepository.get(login).isEmpty()) {
            Player player = new Player(login, password);
            playerRepository.create(player);
            LogService.add("New player creation. Success.");
            return true;
        } else {
            LogService.add("New player creation. Error.");
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
        Optional<Player> player = playerRepository.get(username);
        if (player.isPresent() && (player.get().getPassword().equals(password))) {
            LogService.add("Player with login " + username + "success authorization.");
            return player;
        } else {
            LogService.add("Player with login " + username + " authorization failed.");
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
        Optional<Player> player = playerRepository.get(login);
        if (player.isPresent()) {
            return player.get().getBalance();
        } else {
            String message = "Player with login " + login + " authorization failed.";
            LogService.add(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Sets the balance for the player with the specified ID.
     *
     * @param id      The ID of the player.
     * @param balance The new balance to be set for the player.
     * @throws RuntimeException If there is an error updating the balance in the database.
     */
    public void setBalance(long id, BigDecimal balance) {
        playerRepository.updateBalance(id, balance);
    }
}