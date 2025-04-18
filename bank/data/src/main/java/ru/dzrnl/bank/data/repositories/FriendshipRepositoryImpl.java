package ru.dzrnl.bank.data.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.dzrnl.bank.business.repositories.FriendshipRepository;
import ru.dzrnl.bank.data.entities.UserEntity;

import java.util.List;

@Repository
public class FriendshipRepositoryImpl implements FriendshipRepository {
    private final SessionFactory sessionFactory;

    public FriendshipRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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

    @Override
    public boolean areFriends(String userLogin, String friendLogin) {
        try (Session session = sessionFactory.openSession()) {
            UserEntity user = getUserByLogin(session, userLogin);
            UserEntity friend = getUserByLogin(session, friendLogin);
            return user.getFriends().contains(friend);
        }
    }

    @Override
    public List<String> findAllFriendsOf(String login) {
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
