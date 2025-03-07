package ru.dzrnl.bank.data.repositories;

import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.repositories.AccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountRepositoryImpl implements AccountRepository {
    private final Map<Long, Account> accounts = new HashMap<>();
    private long idCounter = 0;

    @Override
    public Account createAccount(String ownerLogin) {
        var account = new Account(nextId(), ownerLogin);
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<Account> findAccountById(long accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    @Override
    public Set<Account> findAllUserAccounts(String ownerLogin) {
        return accounts.values().stream()
                .filter(account -> account.getOwnerLogin().equals(ownerLogin))
                .collect(Collectors.toSet());
    }

    private long nextId() {
        return idCounter++;
    }
}
