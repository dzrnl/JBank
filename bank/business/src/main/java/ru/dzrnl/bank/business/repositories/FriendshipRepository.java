package ru.dzrnl.bank.business.repositories;

import java.util.List;

public interface FriendshipRepository {
    void addFriend(String userLogin, String friendLogin);

    void removeFriend(String userLogin, String friendLogin);

    boolean areFriends(String userLogin, String friendLogin);

    List<String> findAllFriendsOf(String login);
}
