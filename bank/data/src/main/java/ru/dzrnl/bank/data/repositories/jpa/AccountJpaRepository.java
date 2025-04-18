package ru.dzrnl.bank.data.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dzrnl.bank.data.entities.AccountEntity;

import java.util.List;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findAllByOwner_Login(String ownerLogin);
}
