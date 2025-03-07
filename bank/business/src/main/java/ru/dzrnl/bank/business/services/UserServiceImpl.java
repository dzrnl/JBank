package ru.dzrnl.bank.business.services;

import ru.dzrnl.bank.business.contracts.UserService;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.business.repositories.UserRepository;

import java.util.NoSuchElementException;
import java.util.Set;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String login, String name, int age, Gender gender, HairColor hairColor) {
        var user = new User(login, name, age, gender, hairColor);
        userRepository.saveUser(user);
        return user;
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login)
                .orElseThrow(() -> new NoSuchElementException("User with login '" + login + "' not found"));
    }

    @Override
    public Set<User> getAllUsers() {
        return userRepository.findAllUsers();
    }
}

