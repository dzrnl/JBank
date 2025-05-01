package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.user.User;

import java.util.List;

public interface FriendshipRepository {
    void addFriend(String userLogin, String friendLogin);

    void removeFriend(String userLogin, String friendLogin);

    boolean areFriends(String userLogin, String friendLogin);

    List<User> findAllFriendsOfById(long userId);

    List<String> findAllFriendsOfByLogin(String login);
}
