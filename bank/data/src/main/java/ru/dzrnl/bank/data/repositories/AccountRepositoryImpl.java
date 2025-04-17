package ru.dzrnl.bank.data.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.repositories.AccountRepository;
import ru.dzrnl.bank.data.entities.AccountEntity;
import ru.dzrnl.bank.data.entities.UserEntity;
import ru.dzrnl.bank.data.mappers.AccountMapper;

import java.util.*;

/**
 * Implementation of the {@link AccountRepository} interface for managing accounts.
 * Uses an in-memory storage ({@code HashMap}) to store accounts.
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final SessionFactory sessionFactory;

    /**
     * Default constructor for AccountRepositoryImpl class.
     */
    public AccountRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Creates a new account for a user.
     *
     * @param ownerLogin the login of the account's owner
     * @return the created Account
     */
    @Override
    public Account createAccount(String ownerLogin) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            UserEntity owner = getUserByLogin(session, ownerLogin);

            AccountEntity account = AccountEntity.builder().owner(owner).build();

            session.persist(account);
            transaction.commit();

            return AccountMapper.toDomain(account);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public void updateAccount(Account account) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            UserEntity owner = getUserByLogin(session, account.getOwnerLogin());

            AccountEntity accountEntity = AccountMapper.toEntity(account, owner);

            session.merge(accountEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Retrieves an account by its unique ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return an {@code Optional} containing the account if found, otherwise an empty {@code Optional}
     */
    @Override
    public Optional<Account> findAccountById(long accountId) {
        try (Session session = sessionFactory.openSession()) {
            AccountEntity account = session.get(AccountEntity.class, accountId);
            if (account == null) return Optional.empty();
            return Optional.of(AccountMapper.toDomain(account));
        }
    }

    /**
     * Retrieves all accounts associated with a specific user.
     *
     * @param ownerLogin the login of the account's owner
     * @return a {@code List} of all accounts belonging to the user
     */
    @Override
    public List<Account> findAllUserAccounts(String ownerLogin) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM AccountEntity WHERE owner.login = :ownerLogin", AccountEntity.class)
                    .setParameter("ownerLogin", ownerLogin)
                    .getResultList().stream().map(AccountMapper::toDomain).toList();
        }
    }

    private UserEntity getUserByLogin(Session session, String login) {
        Query<UserEntity> query = session.createQuery(
                "FROM UserEntity u WHERE u.login = :login", UserEntity.class
        );
        query.setParameter("login", login);
        return query.uniqueResult();
    }
}
