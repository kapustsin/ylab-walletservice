package com.ylab.walletservice.domain;

public class Transaction {
    long id;
    long creatorId;
    int amount;
    String type;

    public Transaction(long id, long creatorId, int amount, String type) {
        this.id = id;
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

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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
                ", creatorId=" + creatorId +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                '}';
    }
}