package ru.dzrnl.bank.data.repositories;

import ru.dzrnl.bank.business.repositories.FriendshipRepository;
import ru.dzrnl.bank.data.entities.Friendship;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FriendshipRepositoryImpl implements FriendshipRepository {
    private final Set<Friendship> friendships = new HashSet<>();

    @Override
    public void addFriend(String userLogin, String friendLogin) {
        friendships.add(Friendship.of(userLogin, friendLogin));
    }

    @Override
    public void removeFriend(String userLogin, String friendLogin) {
        friendships.remove(Friendship.of(userLogin, friendLogin));
    }

    @Override
    public Set<String> findFriendLogins(String login) {
        return friendships.stream()
                .filter(f -> f.containsUser(login))
                .map(f -> f.getFriendFor(login))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean areFriends(String userLogin, String friendLogin) {
        return friendships.contains(Friendship.of(userLogin, friendLogin));
    }
}
