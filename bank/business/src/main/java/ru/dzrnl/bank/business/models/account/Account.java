package ru.dzrnl.bank.business.models.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
    private final long id;
    private long balance;
    private final String ownerLogin;
    private final List<Transaction> transactionHistory;

    public Account(long id, String ownerLogin) {
        this.balance = 0;

        this.id = id;
        this.ownerLogin = ownerLogin;
        this.transactionHistory = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account other = (Account) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public long getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    void applyTransaction(Transaction transaction) {
        switch (transaction.type()) {
            case DEPOSIT:
                balance += transaction.amount();
                break;
            case WITHDRAW:
                if (balance < transaction.amount()) {
                    throw new IllegalStateException("Insufficient funds for account " + id);
                }
                balance -= transaction.amount();
                break;
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + transaction.type());
        }
        transactionHistory.add(transaction);
    }
}
