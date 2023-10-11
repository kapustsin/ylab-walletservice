package com.ylab.walletservice.domain.repository;

import com.ylab.walletservice.domain.Transaction;

public interface TransactionRepository {

    boolean exists(long id);

    Transaction get(long id);

    Transaction create (long id, long creatorId, int amount, String type);

    public long getTransactionsSize();

}