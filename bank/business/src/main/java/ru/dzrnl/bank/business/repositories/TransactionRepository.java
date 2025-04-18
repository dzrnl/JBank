package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction save(long accountId, long amount, TransactionType type);

    Optional<Transaction> findById(long transactionId);

    List<Transaction> findAllByAccountId(long accountId);

    List<Transaction> findAll();
}
