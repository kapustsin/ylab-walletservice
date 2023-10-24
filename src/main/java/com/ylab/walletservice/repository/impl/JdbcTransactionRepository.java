package com.ylab.walletservice.repository.impl;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.repository.TransactionRepository;
import com.ylab.walletservice.repository.utils.ConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class JdbcTransactionRepository implements TransactionRepository {
    private final Properties queries = new Properties();
    private final ConnectionManager connectionManager;

    public JdbcTransactionRepository() {
        this.connectionManager = ConnectionManager.getInstance();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("transaction_query.properties")) {
            queries.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connectionManager.getConnection();
    }

    @Override
    public Optional<Transaction> get(long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(queries.getProperty("SELECT_BY_ID"))) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Transaction transaction =
                            new Transaction(resultSet.getLong("id"), resultSet.getLong("token"),
                                    resultSet.getLong("creator_id"), resultSet.getBigDecimal("amount"),
                                    resultSet.getString("type"));
                    return Optional.of(transaction);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long create(Transaction transaction) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(queries.getProperty("CREATE"),
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, transaction.getToken());
            statement.setLong(2, transaction.getCreatorId());
            statement.setBigDecimal(3, transaction.getAmount());
            statement.setString(4, transaction.getType());
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
    public boolean isTransactionTokenUnique(long token) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(queries.getProperty("SELECT_TOKEN"))) {
            statement.setLong(1, token);
            try (ResultSet resultSet = statement.executeQuery()) {
                return !resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> getHistory(long playerId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(queries.getProperty("SELECT_BY_CREATOR_ID"))) {
            statement.setLong(1, playerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    List<Transaction> transactions = new ArrayList<>();
                    do {
                        transactions.add(new Transaction(resultSet.getLong("id"), resultSet.getLong("token"),
                                resultSet.getLong("creator_id"), resultSet.getBigDecimal("amount"),
                                resultSet.getString("type")));
                    } while (resultSet.next());
                    return transactions;
                } else {
                    return Collections.emptyList();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}