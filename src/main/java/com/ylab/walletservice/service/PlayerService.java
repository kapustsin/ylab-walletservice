package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public boolean create(String login, String password) {
        if (!playerRepository.exists(login)) {
            playerRepository.create(login, password);
            LogService.add("New user creation. Success.");
            return true;
        } else {
            LogService.add("New user creation. Error.");
            return false;
        }
    }

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

    public double getBalance(String login) {
        return playerRepository.get(login).getBalance();
    }
}