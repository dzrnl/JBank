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

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final SessionFactory sessionFactory;

    public AccountRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Account save(Account account) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            UserEntity owner = getUserByLogin(session, account.getOwnerLogin());

            AccountEntity entity = AccountMapper.toEntity(account, owner);

            session.persist(entity);
            transaction.commit();

            return AccountMapper.toDomain(entity);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public Optional<Account> findById(long accountId) {
        try (Session session = sessionFactory.openSession()) {
            AccountEntity account = session.get(AccountEntity.class, accountId);
            if (account == null) return Optional.empty();
            return Optional.of(AccountMapper.toDomain(account));
        }
    }

    @Override
    public List<Account> findAllByOwnerLogin(String ownerLogin) {
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
