package com.ylab.walletservice.domain.repository;

import com.ylab.walletservice.domain.Player;

public interface PlayerRepository {

    boolean exists(String login);

    Player get(String login);

    long create(String login, String password);

}