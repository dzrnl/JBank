package ru.dzrnl.apigateway.business.repositories;

import ru.dzrnl.apigateway.business.models.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByLogin(String login);
}
