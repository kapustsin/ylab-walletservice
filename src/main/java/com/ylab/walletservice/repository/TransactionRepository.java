package com.ylab.walletservice.repository;

import com.ylab.walletservice.domain.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * An interface representing a repository for managing transactions.
 */
public interface TransactionRepository {

    /**
     * Retrieves a transaction with the given ID from the repository, if present.
     *
     * @param id The ID of the transaction to retrieve.
     * @return Optional<Transaction> containing a transaction with the given ID,
     * or an empty Optional if no transaction with the specified ID is found.
     */
    Optional<Transaction> get(long id);

    /**
     * Creates a new transaction based on the provided transaction object.
     *
     * @param transaction The transaction object to be created.
     * @return The created transaction ID.
     */
    long create(Transaction transaction);

    /**
     * Checks if the given transaction token is unique within the repository.
     *
     * @param token The token of the transaction to check for uniqueness.
     * @return true if the transaction ID is unique, false otherwise.
     */
    boolean isTransactionTokenUnique(long token);

    /**
     * Retrieves the transaction history for a specific player identified by ID.
     *
     * @param playerId The ID of the player.
     * @return List of Transaction objects representing the player's transaction history,
     *         ordered chronologically from the latest to the earliest transaction.
     */
    List<Transaction> getHistory (long playerId);
}