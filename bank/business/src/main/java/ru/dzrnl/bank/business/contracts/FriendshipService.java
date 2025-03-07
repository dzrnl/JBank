package ru.dzrnl.bank.business.contracts;

import java.util.Set;

public interface FriendshipService {
    void addFriend(String userLogin, String friendLogin);

    void removeFriend(String userLogin, String friendLogin);

    boolean areFriends(String userLogin, String friendLogin);

    Set<String> getFriendLogins(String userLogin);
}
