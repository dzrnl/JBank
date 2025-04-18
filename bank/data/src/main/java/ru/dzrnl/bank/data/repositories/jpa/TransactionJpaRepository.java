package ru.dzrnl.bank.data.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dzrnl.bank.data.entities.TransactionEntity;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllByAccount_Id(Long accountId);
}
