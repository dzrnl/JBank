package ru.dzrnl.bank.data.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.business.repositories.UserRepository;
import ru.dzrnl.bank.data.entities.UserEntity;
import ru.dzrnl.bank.data.mappers.UserMapper;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserEntity entity = UserMapper.toEntity(user);
            session.persist(entity);
            transaction.commit();
            return UserMapper.toDomain(entity);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            Query<UserEntity> query = session.createQuery(
                    "FROM UserEntity WHERE login = :login", UserEntity.class);
            query.setParameter("login", login);

            return query.getResultList().stream().findFirst().map(UserMapper::toDomain);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<UserEntity> query = session.createQuery("FROM UserEntity", UserEntity.class);

            return query.getResultList().stream().map(UserMapper::toDomain).toList();
        }
    }
}
