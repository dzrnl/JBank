package ru.dzrnl.bank.data.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dzrnl.bank.data.entities.UserEntity;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLogin(String login);
}
