package ru.dzrnl.bank.data.repositories;

import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;
import ru.dzrnl.bank.business.repositories.TransactionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link TransactionRepository} for managing transactions.
 * Uses an in-memory storage ({@code HashMap}) to store transactions.
 */
public class TransactionRepositoryImpl implements TransactionRepository {
    private final Map<Long, Transaction> transactions = new HashMap<>();
    private long idCounter = 0;

    /**
     * Creates and stores a new transaction.
     *
     * @param accountId the account ID for which the transaction is being created
     * @param amount    the transaction amount
     * @param type      the type of transaction (either DEPOSIT or WITHDRAW)
     * @return the created transaction
     */
    @Override
    public Transaction createTransaction(long accountId, long amount, TransactionType type) {
        var transaction = new Transaction(nextId(), accountId, amount, type);
        transactions.put(transaction.id(), transaction);
        return transaction;
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return an {@code Optional} containing the transaction if found, otherwise an empty {@code Optional}
     */
    @Override
    public Optional<Transaction> findTransaction(long transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    /**
     * Retrieves all transactions associated with a specific account.
     *
     * @param accountId the ID of the account whose transactions are to be retrieved
     * @return a {@code List} of transactions associated with the provided account
     */
    @Override
    public List<Transaction> findAllAccountTransactions(long accountId) {
        return transactions.values().stream()
                .filter(transaction -> transaction.accountId() == accountId)
                .toList();
    }

    /**
     * Generates a new unique transaction ID.
     *
     * @return a unique transaction ID
     */
    private long nextId() {
        return idCounter++;
    }
}
