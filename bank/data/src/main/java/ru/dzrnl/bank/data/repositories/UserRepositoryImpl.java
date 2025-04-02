package ru.dzrnl.bank.data.repositories;

import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.business.repositories.UserRepository;

import java.util.*;

/**
 * Implementation of {@link UserRepository} for managing users.
 * Uses an in-memory storage ({@code HashMap}) to store users.
 */
public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    /**
     * Default constructor for UserRepositoryImpl class.
     */
    public UserRepositoryImpl() {
    }

    /**
     * Saves a user in the repository.
     *
     * @param user the user to save
     * @throws IllegalArgumentException if a user with the same login already exists
     */
    @Override
    public void saveUser(User user) {
        if (users.containsKey(user.login())) {
            throw new IllegalArgumentException("User with login '" + user.login() + "' already exists.");
        }
        users.put(user.login(), user);
    }

    /**
     * Finds a user by their login.
     *
     * @param login the login of the user to find
     * @return an {@code Optional} containing the user if found, otherwise an empty {@code Optional}
     */
    @Override
    public Optional<User> findUserByLogin(String login) {
        return Optional.ofNullable(users.get(login));
    }

    /**
     * Finds all users in the repository.
     *
     * @return a {@code List} of all users
     */
    @Override
    public List<User> findAllUsers() {
        return users.values().stream().toList();
    }
}
