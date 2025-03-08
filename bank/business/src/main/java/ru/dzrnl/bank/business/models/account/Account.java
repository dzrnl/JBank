package ru.dzrnl.bank.business.models.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a bank account belonging to a user.
 */
public class Account {
    private final long id;
    private long balance;
    private final String ownerLogin;
    private final List<Transaction> transactionHistory;

    /**
     * Creates a new account with zero balance.
     *
     * @param id         unique account identifier
     * @param ownerLogin login of the account owner
     */
    public Account(long id, String ownerLogin) {
        this.balance = 0;
        this.id = id;
        this.ownerLogin = ownerLogin;
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Checks if two accounts are equal based on their ID.
     *
     * @param obj object to compare with
     * @return true if the IDs match, otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account other = (Account) obj;
        return id == other.id;
    }

    /**
     * Returns hash code based on the account ID.
     *
     * @return hash code of the ID
     */
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    /**
     * Returns the account ID for this account.
     *
     * @return the account ID
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the current balance for this account.
     *
     * @return current account balance
     */
    public long getBalance() {
        return balance;
    }

    /**
     * Returns the owner's login for this account.
     *
     * @return owner's login
     */
    public String getOwnerLogin() {
        return ownerLogin;
    }

    /**
     * Returns the transaction history as an unmodifiable list.
     *
     * @return list of all applied transactions
     */
    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    /**
     * Applies a transaction to the account, modifying the balance.
     * <p>
     * This method is package-private to ensure transactions are applied only via the correct business logic flow.
     *
     * @param transaction the transaction to apply
     * @throws IllegalStateException    if a withdrawal is attempted with insufficient funds
     * @throws IllegalArgumentException if the transaction type is unknown
     */
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
