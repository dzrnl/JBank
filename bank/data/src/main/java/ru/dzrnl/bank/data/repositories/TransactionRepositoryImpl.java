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

/**
 * Implementation of {@link TransactionRepository} for managing transactions.
 * Uses an in-memory storage ({@code HashMap}) to store transactions.
 */
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private final SessionFactory sessionFactory;

    /**
     * Default constructor for TransactionRepositoryImpl class.
     */
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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

    /**
     * Retrieves a transaction by its ID.
     *
     * @param transactionId the ID of the transaction to retrieve
     * @return an {@code Optional} containing the transaction if found, otherwise an empty {@code Optional}
     */
    @Override
    public Optional<Transaction> findTransaction(long transactionId) {
        try (Session session = sessionFactory.openSession()) {
            TransactionEntity transaction = session.get(TransactionEntity.class, transactionId);
            if (transaction == null) return Optional.empty();
            return Optional.of(TransactionMapper.toDomain(transaction));
        }
    }

    /**
     * Retrieves all transactions associated with a specific account.
     *
     * @param accountId the ID of the account whose transactions are to be retrieved
     * @return a {@code List} of transactions associated with the provided account
     */
    @Override
    public List<Transaction> findAllAccountTransactions(long accountId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM TransactionEntity t WHERE t.account.id = :accountId", TransactionEntity.class)
                    .setParameter("accountId", accountId)
                    .getResultList().stream().map(TransactionMapper::toDomain).toList();
        }
    }
}
