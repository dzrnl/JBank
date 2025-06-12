package ru.dzrnl.apigateway.data.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dzrnl.apigateway.data.entities.UserEntity;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
}
