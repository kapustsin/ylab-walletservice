package com.ylab.walletservice.service;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.repository.TransactionRepository;
import com.ylab.walletservice.infrastructure.InMemoryPlayerRepository;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PlayerService playerService;
    private final List<Transaction> history = new ArrayList<>();

    public TransactionService(TransactionRepository transactionRepository) {
        this.playerService = new PlayerService(new InMemoryPlayerRepository());
        this.transactionRepository = transactionRepository;
    }

    public boolean create(Player player, int amount, String type) {
        long id = generateTransactionId();
        if (isTransactionIdUnique(id)) {
            if ("debit".equals(type) && checkDebit(player.getLogin(), amount)) {
                history.add(transactionRepository.create(id, player.getId(), amount, type));
                player.setBalance(player.getBalance() - amount);
                LogService.add("Debit transaction success. Id = " + id);
                return true;
            } else if ("credit".equals(type)) {
                history.add(transactionRepository.create(id, player.getId(), amount, type));
                player.setBalance(player.getBalance() + amount);
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

    public List<Transaction> getHistory() {
        LogService.add("User get transaction history");
        return history;
    }

    private boolean checkDebit(String login, int amount) {
        double balance = playerService.getBalance(login);
        LogService.add("User check balance = " + balance);
        return balance - amount >= 0;
    }

    private long generateTransactionId() {
        LogService.add("New transaction id generated.");
        return transactionRepository.getTransactionsSize() + 1;
    }

    private boolean isTransactionIdUnique(long id) {
        LogService.add("Check transaction unique id.");
        return !transactionRepository.exists(id);
    }
}