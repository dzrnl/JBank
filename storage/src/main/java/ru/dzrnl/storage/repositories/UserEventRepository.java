package ru.dzrnl.storage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dzrnl.storage.entities.UserEvent;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
}