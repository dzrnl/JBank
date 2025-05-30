package ru.dzrnl.storage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dzrnl.storage.entities.TransactionEvent;

@Repository
public interface TransactionEventRepository extends JpaRepository<TransactionEvent, Long> {
}
