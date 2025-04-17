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

/**
 * Implementation of {@link UserRepository} for PostgreSQL using Hibernate.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    private final SessionFactory sessionFactory;

    /**
     * Constructs a UserRepositoryImpl.
     *
     * @param sessionFactory Hibernate session factory
     */
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Saves a user in the database.
     *
     * @param user the user to save
     * @throws RuntimeException if an error occurs during the transaction
     */
    @Override
    public void saveUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            UserEntity entity = UserMapper.toEntity(user);
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Finds a user by their login in the database.
     *
     * @param login the login of the user to find
     * @return an {@code Optional} containing the user if found, otherwise an empty {@code Optional}
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public Optional<User> findUserByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            Query<UserEntity> query = session.createQuery(
                    "FROM UserEntity WHERE login = :login", UserEntity.class);
            query.setParameter("login", login);

            return query.getResultList().stream().findFirst().map(UserMapper::toDomain);
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a {@code List} of all users
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public List<User> findAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<UserEntity> query = session.createQuery("FROM UserEntity", UserEntity.class);

            return query.getResultList().stream().map(UserMapper::toDomain).toList();
        }
    }
}
