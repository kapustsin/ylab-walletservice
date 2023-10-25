package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import com.ylab.walletservice.domain.mapper.TransactionMapper;
import com.ylab.walletservice.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Service class for managing transaction operations such as creating transactions and retrieving transaction history.
 */
public class TransactionService {
    /**
     * The mapper for converting between TransactionRequestDto and Transaction objects.
     */
    private final TransactionMapper transactionMapper = TransactionMapper.MAPPER;
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
     * Creates a new transaction based on the provided TransactionRequestDto.
     *
     * @param transactionRequest The TransactionRequestDto containing transaction details.
     * @return true if the transaction is successful, false otherwise.
     */
    public boolean create(TransactionRequestDto transactionRequest) {
        if (isTransactionTokenUnique(transactionRequest.token())) {
            if ("debit".equals(transactionRequest.type()) &&
                    checkDebit(transactionRequest.creatorId(), transactionRequest.amount())) {
                Transaction transaction =
                        transactionMapper.transactionDtoToTransaction(transactionRequest, new Transaction());
                long id = transactionRepository.create(transaction);
                playerService.setBalance(transactionRequest.creatorId(),
                        playerService.getBalance(transactionRequest.creatorId()).subtract(transactionRequest.amount()));
                LogService.add("Debit transaction success. Id = " + id);
                return true;
            } else if ("credit".equals(transactionRequest.type())) {
                Transaction transaction = new Transaction();
                transactionMapper.transactionDtoToTransaction(transactionRequest, transaction);
                long id = transactionRepository.create(transaction);
                playerService.setBalance(transactionRequest.creatorId(),
                        playerService.getBalance(transactionRequest.creatorId()).add(transactionRequest.amount()));
                LogService.add("Credit transaction success. Id = " + id);
                return true;
            } else {
                LogService.add("Create transaction failed. Token = " + transactionRequest.token() + ", player id = " +
                        transactionRequest.creatorId() +
                        ", amount = " + transactionRequest.amount() + ", type = " + transactionRequest.type());
                return false;
            }
        } else {
            LogService.add("Create transaction failed. Token = " + transactionRequest.token() + " not unique.");
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
     * @param playerId The ID of the player.
     * @param amount   The amount of the transaction.
     * @return true if the debit is valid, false otherwise.
     */
    private boolean checkDebit(long playerId, BigDecimal amount) {
        BigDecimal balance = playerService.getBalance(playerId);
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