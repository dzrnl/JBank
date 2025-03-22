package ru.dzrnl.bank.business.contracts;

import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;

import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Provides operations related to user management.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param login     unique login for the user
     * @param name      the name of the user
     * @param age       the age of the user
     * @param gender    the gender of the user
     * @param hairColor the hair color of the user
     * @return the created user
     * @throws IllegalArgumentException if a user with the same login already exists
     * @throws RuntimeException         if there is an error while saving the user
     */
    User createUser(String login, String name, int age, Gender gender, HairColor hairColor);

    /**
     * Retrieves a user by their login.
     *
     * @param login the login of the user to retrieve
     * @return the user associated with the provided login
     * @throws NoSuchElementException if no user with the given login is found
     */
    User getUserByLogin(String login);

    /**
     * Retrieves all users.
     *
     * @return a {@code Set} of all users
     */
    Set<User> getAllUsers();
}
