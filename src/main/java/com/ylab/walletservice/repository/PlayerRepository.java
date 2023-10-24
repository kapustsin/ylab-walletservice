package com.ylab.walletservice.repository;

import com.ylab.walletservice.domain.Player;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * An interface representing a repository for managing player data.
 */
public interface PlayerRepository {

    /**
     * Creates a new player with the given login and password and adds it to the repository.
     *
     * @param player The player object to be created.
     * @return The ID of the created player.
     */
    long create(Player player);

    /**
     * Retrieves the player with the specified login from the repository, if present.
     *
     * @param login The login of the player to retrieve.
     * @return Optional<Player> containing the player with the specified login,
     * or an empty Optional if no player with the specified login is found.
     */
    Optional<Player> get(String login);

    /**
     * Updates the balance of a player with the given ID.
     *
     * @param id      The ID of the player whose balance needs to be updated.
     * @param balance The new balance value to set for the player.
     */
    void updateBalance(long id, BigDecimal balance);

}