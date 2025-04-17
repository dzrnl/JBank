package ru.dzrnl.bank.business.services;

import org.springframework.stereotype.Service;
import ru.dzrnl.bank.business.contracts.UserService;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.business.repositories.UserRepository;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Implementation of {@link UserService} for managing users.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Creates a new instance of {@code UserServiceImpl} with the provided {@link UserRepository}.
     *
     * @param userRepository the repository for managing users
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user and saves it in the repository.
     *
     * @param login     the login for the new user
     * @param name      the name of the new user
     * @param age       the age of the new user
     * @param gender    the gender of the new user
     * @param hairColor the hair color of the new user
     * @return the created user
     * @throws IllegalArgumentException if a user with the same login already exists
     * @throws RuntimeException         if there is an error while saving the user
     */
    @Override
    public User createUser(String login, String name, int age, Gender gender, HairColor hairColor) {
        var user = User.builder()
                .login(login)
                .name(name)
                .age(age)
                .gender(gender)
                .hairColor(hairColor)
                .build();

        try {
            userRepository.saveUser(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("User with login '" + login + "' already exists", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error occurred while saving user", e);
        }
        return user;
    }

    /**
     * Retrieves a user by their login.
     *
     * @param login the login of the user to retrieve
     * @return the user associated with the provided login
     * @throws NoSuchElementException if no user with the given login is found
     */
    @Override
    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login)
                .orElseThrow(() -> new NoSuchElementException("User with login '" + login + "' not found"));
    }

    /**
     * Retrieves all users.
     *
     * @return a {@code Set} of all users
     */
    @Override
    public Set<User> getAllUsers() {
        return new HashSet<>(userRepository.findAllUsers());
    }
}
