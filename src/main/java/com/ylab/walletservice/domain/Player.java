package com.ylab.walletservice.domain;

import java.math.BigDecimal;

/**
 * Represents a player entity with unique ID, login, password, and balance.
 */
public class Player {
    long id;
    String login;
    String password;
    BigDecimal balance;

    public Player() {
    }

    public Player(long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.balance = new BigDecimal(0);
    }

    public Player(String login, String password) {
        this.login = login;
        this.password = password;
        this.balance = new BigDecimal(0);
    }

    public Player(long id, String login, String password, BigDecimal balance) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Player player = (Player) o;

        return getId() == player.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}