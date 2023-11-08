package com.ylab.walletservice.repository.impl;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTransactionRepository implements TransactionRepository {
    private final DataSource dataSource;

    public JdbcTransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Transaction> get(long id) {
        final String SQL_SELECT_BY_ID =
                "SELECT id, token, creator_id, amount, type FROM walletservice.transaction WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public long create(Transaction transaction) {
        final String SQL_CREATE =
                "INSERT INTO walletservice.transaction (token, creator_id, amount, type) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_CREATE,
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTransactionTokenUnique(long token) {
        final String SQL_SELECT_TOKEN = "SELECT token FROM walletservice.transaction WHERE token = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_TOKEN)) {
            statement.setLong(1, token);
            try (ResultSet resultSet = statement.executeQuery()) {
                return !resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> getHistory(long playerId) {
        final String SQL_SELECT_BY_CREATOR_ID =
                "SELECT id, token, creator_id, amount, type FROM walletservice.transaction WHERE creator_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_CREATOR_ID)) {
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