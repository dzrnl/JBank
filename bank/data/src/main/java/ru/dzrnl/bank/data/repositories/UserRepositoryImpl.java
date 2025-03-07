package ru.dzrnl.bank.data.repositories;

import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.business.repositories.UserRepository;

import java.util.*;

public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) {
        if (users.containsKey(user.getLogin())) {
            throw new IllegalArgumentException("User with login '" + user.getLogin() + "' already exists.");
        }
        users.put(user.getLogin(), user);
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        return Optional.ofNullable(users.get(login));
    }

    @Override
    public Set<User> findAllUsers() {
        return new HashSet<>(users.values());
    }
}
