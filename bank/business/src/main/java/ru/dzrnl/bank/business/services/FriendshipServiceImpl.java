package ru.dzrnl.bank.business.services;

import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.repositories.FriendshipRepository;

import java.util.Set;

public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;

    public FriendshipServiceImpl(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public void addFriend(String userLogin, String friendLogin) {
        friendshipRepository.addFriend(userLogin, friendLogin);
    }

    @Override
    public void removeFriend(String userLogin, String friendLogin) {
        friendshipRepository.removeFriend(userLogin, friendLogin);
    }

    @Override
    public boolean areFriends(String userLogin, String friendLogin) {
        return friendshipRepository.areFriends(userLogin, friendLogin);
    }

    @Override
    public Set<String> getFriendLogins(String userLogin) {
        return friendshipRepository.findFriendLogins(userLogin);
    }
}
