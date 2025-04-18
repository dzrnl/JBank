package ru.dzrnl.bank.data.repositories;

import org.springframework.stereotype.Repository;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;
import ru.dzrnl.bank.business.repositories.TransactionRepository;
import ru.dzrnl.bank.data.entities.AccountEntity;
import ru.dzrnl.bank.data.entities.TransactionEntity;
import ru.dzrnl.bank.data.mappers.TransactionMapper;
import ru.dzrnl.bank.data.repositories.jpa.AccountJpaRepository;
import ru.dzrnl.bank.data.repositories.jpa.TransactionJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private final TransactionJpaRepository transactionJpaRepository;
    private final AccountJpaRepository accountJpaRepository;

    public TransactionRepositoryImpl(TransactionJpaRepository transactionJpaRepository,
                                     AccountJpaRepository accountJpaRepository) {
        this.transactionJpaRepository = transactionJpaRepository;
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public Transaction save(long accountId, long amount, TransactionType type) {
        AccountEntity account = accountJpaRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        TransactionEntity transactionEntity = TransactionEntity.builder()
                .account(account)
                .amount(amount)
                .type(type)
                .build();

        TransactionEntity saved = transactionJpaRepository.save(transactionEntity);
        return TransactionMapper.toDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(long transactionId) {
        return transactionJpaRepository.findById(transactionId)
                .map(TransactionMapper::toDomain);
    }

    @Override
    public List<Transaction> findAllByAccountId(long accountId) {
        return transactionJpaRepository.findAllByAccount_Id(accountId).stream()
                .map(TransactionMapper::toDomain)
                .toList();
    }
}
