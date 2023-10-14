package com.ylab.walletservice.domain.repository;

import com.ylab.walletservice.domain.Transaction;

/**
 * An interface representing a repository for managing transactions.
 */
public interface TransactionRepository {

    /**
     * Checks if a transaction with the given ID exists in the repository.
     *
     * @param id The ID of the transaction to check.
     * @return true if the transaction exists, false otherwise.
     */
    boolean exists(long id);

    /**
     * Retrieves a transaction with the given ID from the repository.
     *
     * @param id The ID of the transaction to retrieve.
     * @return The Transaction object if found, or null if not found.
     */
    Transaction get(long id);

    /**
     * Creates a new transaction based on the provided transaction object.
     *
     * @param transaction The transaction object to be created.
     * @return The created transaction object.
     */
    Transaction create(Transaction transaction);

    /**
     * Retrieves the total number of transactions in the repository.
     *
     * @return The total number of transactions.
     */
    long getTransactionsSize();

}