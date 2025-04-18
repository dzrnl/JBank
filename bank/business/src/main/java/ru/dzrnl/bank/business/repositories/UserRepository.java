package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(long userId);

    Optional<User> findByLogin(String login);

    List<User> findAll();
}
