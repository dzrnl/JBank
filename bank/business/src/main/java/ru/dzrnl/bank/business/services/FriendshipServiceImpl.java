package ru.dzrnl.bank.business.services;

import org.springframework.stereotype.Service;
import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.repositories.FriendshipRepository;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link FriendshipService} for managing user friendships.
 */
@Service
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipRepository friendshipRepository;

    /**
     * Creates a new instance of {@code FriendshipServiceImpl} with the provided {@link FriendshipRepository}.
     *
     * @param friendshipRepository the repository for managing friendships
     */
    public FriendshipServiceImpl(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    /**
     * Adds a friendship between two users.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     */
    @Override
    public void addFriend(String userLogin, String friendLogin) {
        friendshipRepository.addFriend(userLogin, friendLogin);
    }

    /**
     * Removes a friendship between two users.
     *
     * @param userLogin   the login of the first user
     * @param friendLogin the login of the second user
     */
    @Override
    public void removeFriend(String userLogin, String friendLogin) {
        friendshipRepository.removeFriend(userLogin, friendLogin);
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
        return friendshipRepository.areFriends(userLogin, friendLogin);
    }

    /**
     * Retrieves all friend logins for a given user.
     *
     * @param userLogin the login of the user whose friends are to be retrieved
     * @return a {@code set} of logins of the user's friends
     */
    @Override
    public Set<String> getFriendLogins(String userLogin) {
        return new HashSet<>(friendshipRepository.findFriendLogins(userLogin));
    }
}
