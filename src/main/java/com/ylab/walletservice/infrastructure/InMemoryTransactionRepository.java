package com.ylab.walletservice.infrastructure;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.repository.TransactionRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryTransactionRepository implements TransactionRepository {
    private static final Map<Long, Transaction> transactions = new HashMap<>();

    @Override
    public Transaction get(long id) {
        return transactions.get(id);
    }

    @Override
    public boolean exists(long id) {
        return transactions.containsKey(id);
    }

    @Override
    public Transaction create(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public long getTransactionsSize() {
        return transactions.size();
    }
}