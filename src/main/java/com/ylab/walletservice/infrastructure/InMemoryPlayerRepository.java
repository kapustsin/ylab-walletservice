package com.ylab.walletservice.infrastructure;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.repository.PlayerRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPlayerRepository implements PlayerRepository {
    private static final Map<String, Player> players = new HashMap<>();

    @Override
    public Player get(String login) {
        return players.get(login);
    }

    @Override
    public boolean exists(String login) {
        return players.containsKey(login);
    }

    @Override
    public long create(Player player) {
        players.put(player.getLogin(), player);
        return player.getId();
    }

    @Override
    public long getPlayersSize() {
        return players.size();
    }
}