package ru.dzrnl.storage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dzrnl.storage.entities.AccountEvent;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEvent, Long> {
}
