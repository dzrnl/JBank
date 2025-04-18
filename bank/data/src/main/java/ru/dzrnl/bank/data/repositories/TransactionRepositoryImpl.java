package ru.dzrnl.bank.data.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;
import ru.dzrnl.bank.business.repositories.TransactionRepository;
import ru.dzrnl.bank.data.entities.AccountEntity;
import ru.dzrnl.bank.data.entities.TransactionEntity;
import ru.dzrnl.bank.data.mappers.TransactionMapper;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private final SessionFactory sessionFactory;

    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Transaction save(long accountId, long amount, TransactionType type) {
        org.hibernate.Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            AccountEntity account = session.get(AccountEntity.class, accountId);

            TransactionEntity transactionEntity = TransactionEntity.builder()
                    .account(account)
                    .amount(amount)
                    .type(type)
                    .build();

            session.persist(transactionEntity);
            transaction.commit();

            return TransactionMapper.toDomain(transactionEntity);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Transaction> findById(long transactionId) {
        try (Session session = sessionFactory.openSession()) {
            TransactionEntity transaction = session.get(TransactionEntity.class, transactionId);
            if (transaction == null) return Optional.empty();
            return Optional.of(TransactionMapper.toDomain(transaction));
        }
    }

    @Override
    public List<Transaction> findAllByAccountId(long accountId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM TransactionEntity t WHERE t.account.id = :accountId", TransactionEntity.class)
                    .setParameter("accountId", accountId)
                    .getResultList().stream().map(TransactionMapper::toDomain).toList();
        }
    }
}
