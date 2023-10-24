package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Service class for managing transaction operations such as creating transactions and retrieving transaction history.
 */
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PlayerService playerService;

    /**
     * Constructs a TransactionService object with the specified TransactionRepository instance.
     *
     * @param transactionRepository The repository used for transaction-related data access.
     */
    public TransactionService(TransactionRepository transactionRepository, PlayerService playerService) {
        this.transactionRepository = transactionRepository;
        this.playerService = playerService;
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
        long token = generateTransactionToken();
        if (isTransactionTokenUnique(token)) {
            if ("debit".equals(type) && checkDebit(player.getLogin(), amount)) {
                long id = transactionRepository.create(new Transaction(token, player.getId(), amount, type));
                playerService.setBalance(player.getId(), playerService.getBalance(player.getLogin()).subtract(amount));
                LogService.add("Debit transaction success. Id = " + id);
                return true;
            } else if ("credit".equals(type)) {
                long id = transactionRepository.create(new Transaction(token, player.getId(), amount, type));
                playerService.setBalance(player.getId(), playerService.getBalance(player.getLogin()).add(amount));
                LogService.add("Credit transaction success. Id = " + id);
                return true;
            } else {
                LogService.add("Create transaction failed. Token = " + token + ", player id = " + player.getId() +
                        ", amount = " + amount + ", type = " + type);
                return false;
            }
        } else {
            LogService.add("Create transaction failed. Token = " + token + " not unique.");
            return false;
        }
    }

    /**
     * Retrieves the transaction history.
     *
     * @param playerId The ID of the player.
     * @return The list of transaction history.
     */
    public List<Transaction> getHistory(long playerId) {
        LogService.add("User get transaction history");
        return transactionRepository.getHistory(playerId);
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
     * Generates a unique transaction token for new transactions.
     *
     * @return The generated token.
     */
    private long generateTransactionToken() {
        LogService.add("New transaction token generated.");
        Random random = new Random();
        return Math.abs(random.nextLong());
    }

    /**
     * Checks if a transaction ID is unique.
     *
     * @param id The transaction ID to check.
     * @return true if the ID is unique, false otherwise.
     */
    private boolean isTransactionTokenUnique(long id) {
        LogService.add("Check transaction unique id.");
        return transactionRepository.isTransactionTokenUnique(id);
    }
}