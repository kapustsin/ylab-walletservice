package com.ylab.walletservice.repository.impl;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.repository.PlayerRepository;
import com.ylab.walletservice.repository.utils.ConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;

public class JdbcPlayerRepository implements PlayerRepository {
    private final Properties queries = new Properties();
    private final ConnectionManager connectionManager;

    public JdbcPlayerRepository() {
        this.connectionManager = ConnectionManager.getInstance();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("player_query.properties")) {
            queries.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connectionManager.getConnection();
    }

    @Override
    public long create(Player player) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(queries.getProperty("CREATE"),
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
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(queries.getProperty("SELECT_BY_LOGIN"));) {
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
    public void updateBalance(long id, BigDecimal balance) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(queries.getProperty("UPDATE_BALANCE"))) {
            statement.setBigDecimal(1, balance);
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}