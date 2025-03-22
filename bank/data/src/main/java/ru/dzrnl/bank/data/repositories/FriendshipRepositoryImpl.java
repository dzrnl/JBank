package ru.dzrnl.bank.data.repositories;

import ru.dzrnl.bank.business.repositories.FriendshipRepository;
import ru.dzrnl.bank.data.entities.Friendship;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link FriendshipRepository} for managing user friendships.
 * Uses an in-memory storage ({@code HashMap}) to store friendships.
 */
public class FriendshipRepositoryImpl implements FriendshipRepository {
    private final Set<Friendship> friendships = new HashSet<>();

    /**
     * Default constructor for FriendshipRepositoryImpl class.
     */
    public FriendshipRepositoryImpl() {
    }

    /**
     * Adds a friendship between two users.
     * A friendship is bidirectional, meaning both users will be linked.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     */
    @Override
    public void addFriend(String userLogin, String friendLogin) {
        friendships.add(Friendship.of(userLogin, friendLogin));
    }

    /**
     * Removes a friendship between two users.
     * Both directions of the friendship are removed.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     */
    @Override
    public void removeFriend(String userLogin, String friendLogin) {
        friendships.remove(Friendship.of(userLogin, friendLogin));
    }

    /**
     * Checks if two users are friends.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     * @return {@code true} if the users are friends, otherwise {@code false}
     */
    @Override
    public boolean areFriends(String userLogin, String friendLogin) {
        return friendships.contains(Friendship.of(userLogin, friendLogin));
    }

    /**
     * Retrieves all friend logins for a given user.
     *
     * @param login the login of the user whose friends are to be retrieved
     * @return a {@code Set} of logins of the user's friends
     */
    @Override
    public Set<String> findFriendLogins(String login) {
        return friendships.stream()
                .filter(f -> f.containsUser(login))
                .map(f -> f.getFriendFor(login))
                .collect(Collectors.toSet());
    }
}
