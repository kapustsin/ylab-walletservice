package com.ylab.walletservice.domain;

import java.math.BigDecimal;

/**
 * Represents a transaction entity with unique ID, creator ID, amount, and type.
 */
public class Transaction {
    long id;
    long token;
    long creatorId;
    BigDecimal amount;
    String type;

    public Transaction() {
    }

    public Transaction(long id, long token, long creatorId, BigDecimal amount, String type) {
        this.id = id;
        this.token = token;
        this.creatorId = creatorId;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(long token, long creatorId, BigDecimal amount, String type) {
        this.token = token;
        this.creatorId = creatorId;
        this.amount = amount;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Transaction that = (Transaction) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", token=" + token +
                ", creatorId=" + creatorId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                '}';
    }
}