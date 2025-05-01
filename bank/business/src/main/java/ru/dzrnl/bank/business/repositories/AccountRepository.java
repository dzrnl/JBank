package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.account.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);

    Optional<Account> findById(long accountId);

    List<Account> findAllByOwnerLogin(String ownerLogin);

    List<Account> findAll();
}
