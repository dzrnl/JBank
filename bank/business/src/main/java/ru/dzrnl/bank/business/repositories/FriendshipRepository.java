package ru.dzrnl.bank.business.repositories;

import java.util.Set;

/**
 * Provides operations for managing user friendships in a repository.
 */
public interface FriendshipRepository {

    /**
     * Adds a friendship between two users.
     * A friendship is bidirectional, meaning both users will be linked.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     */
    void addFriend(String userLogin, String friendLogin);

    /**
     * Removes a friendship between two users.
     * Both directions of the friendship are removed.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     */
    void removeFriend(String userLogin, String friendLogin);

    /**
     * Checks if two users are friends.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     * @return {@code true} if the users are friends, otherwise {@code false}
     */
    boolean areFriends(String userLogin, String friendLogin);

    /**
     * Retrieves all friend logins for a given user.
     *
     * @param login the login of the user whose friends are to be retrieved
     * @return a {@code Set} of logins of the user's friends
     */
    Set<String> findFriendLogins(String login);
}
