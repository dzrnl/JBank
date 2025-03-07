package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.user.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository {
    void saveUser(User user);

    Optional<User> findUserByLogin(String login);

    Set<User> findAllUsers();
}
