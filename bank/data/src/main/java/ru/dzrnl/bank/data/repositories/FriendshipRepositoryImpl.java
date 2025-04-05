package ru.dzrnl.bank.data.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.dzrnl.bank.business.repositories.FriendshipRepository;
import ru.dzrnl.bank.data.entities.UserEntity;

import java.util.List;

/**
 * Implementation of {@link FriendshipRepository} for PostgreSQL using Hibernate.
 */
public class FriendshipRepositoryImpl implements FriendshipRepository {
    private final SessionFactory sessionFactory;

    /**
     * Constructs a FriendshipRepositoryImpl.
     *
     * @param sessionFactory Hibernate session factory
     */
    public FriendshipRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Adds a friendship between two users.
     * A friendship is bidirectional, meaning both users will be linked.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     * @throws IllegalArgumentException if either user or friend is not found
     * @throws RuntimeException if an error occurs during the transaction
     */
    @Override
    public void addFriend(String userLogin, String friendLogin) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            UserEntity user = getUserByLogin(session, userLogin);
            UserEntity friend = getUserByLogin(session, friendLogin);

            user.getFriends().add(friend);
            friend.getFriends().add(user);

            session.persist(user);
            session.persist(friend);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Removes a friendship between two users.
     * Both directions of the friendship are removed.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     * @throws IllegalArgumentException if either user or friend is not found
     * @throws RuntimeException if a database access error occurs
     */
    @Override
    public void removeFriend(String userLogin, String friendLogin) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            UserEntity user = getUserByLogin(session, userLogin);
            UserEntity friend = getUserByLogin(session, friendLogin);

            user.getFriends().remove(friend);
            friend.getFriends().remove(user);

            session.persist(user);
            session.persist(friend);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    /**
     * Checks if two users are friends.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     * @return {@code true} if the users are friends, otherwise {@code false}
     * @throws IllegalArgumentException if either user or friend is not found
     */
    @Override
    public boolean areFriends(String userLogin, String friendLogin) {
        try (Session session = sessionFactory.openSession()) {
            UserEntity user = getUserByLogin(session, userLogin);
            UserEntity friend = getUserByLogin(session, friendLogin);
            return user.getFriends().contains(friend);
        }
    }

    /**
     * Retrieves all friend logins for a given user.
     *
     * @param login the login of the user whose friends are to be retrieved
     * @return a {@code List} of logins of the user's friends
     * @throws IllegalArgumentException if either user or friend is not found
     */
    @Override
    public List<String> findFriendLogins(String login) {
        try (Session session = sessionFactory.openSession()) {
            UserEntity user = getUserByLogin(session, login);

            return user.getFriends().stream()
                    .map(UserEntity::getLogin)
                    .toList();
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
