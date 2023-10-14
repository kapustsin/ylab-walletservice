package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.repository.TransactionRepository;
import com.ylab.walletservice.infrastructure.InMemoryPlayerRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing transaction operations such as creating transactions and retrieving transaction history.
 */
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PlayerService playerService;
    private final List<Transaction> history = new ArrayList<>();

    /**
     * Constructs a TransactionService object with the specified TransactionRepository instance.
     *
     * @param transactionRepository The repository used for transaction-related data access.
     */
    public TransactionService(TransactionRepository transactionRepository) {
        this.playerService = new PlayerService(new InMemoryPlayerRepository());
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates a new transaction for the given player with the specified amount and type.
     *
     * @param player The player initiating the transaction.
     * @param amount The amount of the transaction.
     * @param type   The type of the transaction ("debit" or "credit").
     * @return true if the transaction is successful, false otherwise.
     */
    public boolean create(Player player, BigDecimal amount, String type) {
        long id = generateTransactionId();
        if (isTransactionIdUnique(id)) {
            if ("debit".equals(type) && checkDebit(player.getLogin(), amount)) {
                history.add(transactionRepository.create(new Transaction(id, player.getId(), amount, type)));
                player.setBalance(player.getBalance().subtract(amount));
                LogService.add("Debit transaction success. Id = " + id);
                return true;
            } else if ("credit".equals(type)) {
                Transaction transaction = new Transaction(id, player.getId(), amount, type);
                history.add(transaction);
                player.setBalance(player.getBalance().add(amount));
                LogService.add("Credit transaction success. Id = " + id);
                return true;
            } else {
                LogService.add("Create transaction failed. Id = " + id + ", type = " + type);
                return false;
            }
        } else {
            LogService.add("Create transaction failed. Id = " + id + "not unique.");
            return false;
        }
    }

    /**
     * Retrieves the transaction history.
     *
     * @return The list of transaction history.
     */
    public List<Transaction> getHistory() {
        LogService.add("User get transaction history");
        return history;
    }

    /**
     * Checks if a debit transaction is valid for the given player.
     *
     * @param login  The login of the player.
     * @param amount The amount of the transaction.
     * @return true if the debit is valid, false otherwise.
     */
    private boolean checkDebit(String login, BigDecimal amount) {
        BigDecimal balance = playerService.getBalance(login);
        LogService.add("User check balance = " + balance.toString());
        return balance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Generates a unique transaction ID for new transactions.
     *
     * @return The generated unique transaction ID.
     */
    private long generateTransactionId() {
        LogService.add("New transaction id generated.");
        return transactionRepository.getTransactionsSize() + 1;
    }

    /**
     * Checks if a transaction ID is unique.
     *
     * @param id The transaction ID to check.
     * @return true if the ID is unique, false otherwise.
     */
    private boolean isTransactionIdUnique(long id) {
        LogService.add("Check transaction unique id.");
        return !transactionRepository.exists(id);
    }
}