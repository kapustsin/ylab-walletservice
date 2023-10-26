package com.ylab.walletservice.service;

import com.ylab.walletservice.aop.annotations.Loggable;
import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import com.ylab.walletservice.domain.mapper.PlayerMapper;
import com.ylab.walletservice.repository.PlayerRepository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service class for managing player operations such as registration, authorization, and balance retrieval.
 */
@Loggable
public class PlayerService {
    /**
     * The mapper for converting between PlayerDto and Player objects.
     */
    private final PlayerMapper playerMapper = PlayerMapper.MAPPER;
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
     * Creates a new player using the provided registration data.
     *
     * @param registrationData The registration data containing player's details.
     * @return true if registration is successful, false otherwise.
     */
    public boolean create(RegistrationDto registrationData) {
        if (playerRepository.get(registrationData.login()).isEmpty()) {
            Player player = playerMapper.registrationDtoToPlayer(registrationData);
            playerRepository.create(player);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Authorizes a player with the given username and password.
     *
     * @param credentials The credentials (login and password) provided by the player.
     * @return An Optional containing the authorized PlayerDto object if successful, or empty otherwise.
     */
    public Optional<LoggedInPlayerDto> doAuthorisation(CredentialsDto credentials) {
        Optional<Player> player = playerRepository.get(credentials.login());
        if (player.isPresent() && (player.get().getPassword().equals(credentials.password()))) {
            return Optional.of(playerMapper.playerToLoggedInPlayerDto(player.get()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieves the balance of the player with the given ID.
     *
     * @param playerId The ID of the player.
     * @return The balance of the player.
     */
    public BigDecimal getBalance(long playerId) {
        return playerRepository.getBalance(playerId);
    }

    /**
     * Sets the balance for the player with the specified ID.
     *
     * @param id      The ID of the player.
     * @param balance The new balance to be set for the player.
     * @throws RuntimeException If there is an error updating the balance in the database.
     */
    void setBalance(long id, BigDecimal balance) {
        LogService.add("Player with id =  " + id + " update balance.");
        playerRepository.updateBalance(id, balance);
    }
}