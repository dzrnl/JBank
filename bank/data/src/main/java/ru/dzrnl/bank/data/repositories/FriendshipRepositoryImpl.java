package ru.dzrnl.bank.data.repositories;

import org.springframework.stereotype.Repository;
import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.business.repositories.FriendshipRepository;
import ru.dzrnl.bank.data.entities.UserEntity;
import ru.dzrnl.bank.data.mappers.UserMapper;
import ru.dzrnl.bank.data.repositories.jpa.UserJpaRepository;

import java.util.List;

@Repository
public class FriendshipRepositoryImpl implements FriendshipRepository {
    private final UserJpaRepository userJpaRepository;

    public FriendshipRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public void addFriend(String userLogin, String friendLogin) {
        UserEntity user = getUserByLogin(userLogin);
        UserEntity friend = getUserByLogin(friendLogin);

        user.getFriends().add(friend);
        friend.getFriends().add(user);

        userJpaRepository.save(user);
        userJpaRepository.save(friend);
    }

    @Override
    public void removeFriend(String userLogin, String friendLogin) {
        UserEntity user = getUserByLogin(userLogin);
        UserEntity friend = getUserByLogin(friendLogin);

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);

        userJpaRepository.save(user);
        userJpaRepository.save(friend);
    }

    @Override
    public boolean areFriends(String userLogin, String friendLogin) {
        UserEntity user = getUserByLogin(userLogin);
        UserEntity friend = getUserByLogin(friendLogin);
        return user.getFriends().contains(friend);
    }

    @Override
    public List<User> findAllFriendsOfById(long userId) {
        UserEntity user = getUserById(userId);
        return user.getFriends().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public List<String> findAllFriendsOfByLogin(String login) {
        UserEntity user = getUserByLogin(login);
        return user.getFriends().stream()
                .map(UserEntity::getLogin)
                .toList();
    }

    private UserEntity getUserById(long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    private UserEntity getUserByLogin(String login) {
        return userJpaRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + login));
    }
}
