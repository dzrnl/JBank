package ru.dzrnl.bank.data.repositories;

import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;
import ru.dzrnl.bank.business.repositories.TransactionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Map<Long, Transaction> transactions = new HashMap<>();
    private long idCounter = 0;

    @Override
    public Transaction createTransaction(long accountId, long amount, TransactionType type) {
        var transaction = new Transaction(nextId(), accountId, amount, type);
        transactions.put(transaction.id(), transaction);
        return transaction;
    }

    @Override
    public Optional<Transaction> findTransaction(long transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    @Override
    public List<Transaction> findAllAccountTransactions(long accountId) {
        return transactions.values().stream()
                .filter(transaction -> transaction.accountId() == accountId)
                .toList();
    }

    private long nextId() {
        return idCounter++;
    }
}
