package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.user.User;

import java.util.List;
import java.util.Optional;

/**
 * Provides operations for managing users in a repository.
 */
public interface UserRepository {

    /**
     * Saves a user to the repository.
     *
     * @param user the user to save
     * @throws IllegalArgumentException if a user with the same login already exists
     */
    void saveUser(User user);

    /**
     * Finds a user by their login.
     *
     * @param login the login of the user to find
     * @return an {@code Optional} containing the user if found, otherwise an empty {@code Optional}
     */
    Optional<User> findUserByLogin(String login);

    /**
     * Finds all users in the repository.
     *
     * @return a {@code List} of all users
     */
    List<User> findAllUsers();
}
