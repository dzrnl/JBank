package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.account.Account;

import java.util.Optional;
import java.util.Set;

public interface AccountRepository {
    Account createAccount(String ownerLogin);

    Optional<Account> findAccountById(long accountId);

    Set<Account> findAllUserAccounts(String ownerLogin);
}
