package com.ylab.walletservice.repository.impl;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.repository.PlayerRepository;
import com.ylab.walletservice.repository.utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class JdbcPlayerRepository implements PlayerRepository {
    private final ConnectionManager connectionManager;

    public JdbcPlayerRepository() {
        this.connectionManager = ConnectionManager.getInstance();
    }

    public Connection getConnection() {
        return connectionManager.getConnection();
    }

    @Override
    public long create(Player player) {
        final String SQL_CREATE = """
                INSERT INTO walletservice.player (login, password, balance) VALUES (?, ?, 0)
                """;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_CREATE,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, player.getLogin());
            statement.setString(2, player.getPassword());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                return generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Player> get(String login) {
        final String SQL_SELECT_BY_LOGIN = """
                SELECT id, login, password, balance FROM walletservice.player WHERE login = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_LOGIN)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Player player = new Player(resultSet.getLong("id"), resultSet.getString("login"),
                            resultSet.getString("password"), resultSet.getBigDecimal("balance"));
                    return Optional.of(player);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal getBalance(long playerId) {
        final String SQL_SELECT_BALANCE_BY_ID = """
                SELECT balance FROM walletservice.player WHERE id = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BALANCE_BY_ID)) {
            statement.setLong(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getBigDecimal(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateBalance(long id, BigDecimal balance) {
        final String SQL_UPDATE_BALANCE = """
                UPDATE walletservice.player SET balance = ? WHERE id = ?
                """;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_BALANCE)) {
            statement.setBigDecimal(1, balance);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}