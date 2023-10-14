package com.ylab.walletservice.domain.repository;

import com.ylab.walletservice.domain.Player;

/**
 * An interface representing a repository for managing player data.
 */
public interface PlayerRepository {
    /**
     * Checks if a player with the given login exists in the repository.
     *
     * @param login The login of the player to check.
     * @return true if the player exists, false otherwise.
     */
    boolean exists(String login);

    /**
     * Retrieves a player with the given login from the repository.
     *
     * @param login The login of the player to retrieve.
     * @return The Player object if found, or null if not found.
     */
    Player get(String login);

    /**
     * Creates a new player with the given login and password and adds it to the repository.
     *
     * @param player The player object to be created.
     * @return The ID of the created player.
     */
    long create(Player player);

    /**
     * Retrieves the total number of players in the repository.
     *
     * @return The total number of players.
     */
    long getPlayersSize();

}