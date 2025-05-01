package ru.dzrnl.bank.business.models.account;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a bank account belonging to a user.
 */
@Data
@AllArgsConstructor
public class Account {
    private final Long id;
    private long balance;
    private final String ownerLogin;
    private final List<Transaction> transactionHistory;

    public Account(String ownerLogin) {
        this.id = null;
        this.balance = 0;
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
        return Objects.equals(id, other.id);
    }

    /**
     * Returns hash code based on the account ID.
     *
     * @return hash code of the ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
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
