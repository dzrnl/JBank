package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;

import java.util.List;
import java.util.Optional;

/**
 * Provides operations for managing transactions in a repository.
 */
public interface TransactionRepository {

    /**
     * Creates a new transaction.
     *
     * @param accountId the account ID for which the transaction is being created
     * @param amount    the transaction amount
     * @param type      the type of transaction (either DEPOSIT or WITHDRAW)
     * @return the created transaction
     */
    Transaction createTransaction(long accountId, long amount, TransactionType type);

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return an {@code Optional} containing the transaction if found, otherwise an empty {@code Optional}
     */
    Optional<Transaction> findTransaction(long transactionId);

    /**
     * Retrieves all transactions associated with a specific account.
     *
     * @param accountId the ID of the account whose transactions are to be retrieved
     * @return a {@code List} of transactions associated with the provided account
     */
    List<Transaction> findAllAccountTransactions(long accountId);
}
