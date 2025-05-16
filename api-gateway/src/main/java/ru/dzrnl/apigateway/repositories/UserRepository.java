package ru.dzrnl.apigateway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dzrnl.apigateway.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
