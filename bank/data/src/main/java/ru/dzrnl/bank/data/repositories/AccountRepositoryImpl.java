package ru.dzrnl.bank.data.repositories;

import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.repositories.AccountRepository;

import java.util.*;

/**
 * Implementation of the {@link AccountRepository} interface for managing accounts.
 * Uses an in-memory storage ({@code HashMap}) to store accounts.
 */
public class AccountRepositoryImpl implements AccountRepository {
    private final Map<Long, Account> accounts = new HashMap<>();
    private long idCounter = 0;

    /**
     * Default constructor for AccountRepositoryImpl class.
     */
    public AccountRepositoryImpl() {
    }

    /**
     * Creates a new account for a user.
     *
     * @param ownerLogin the login of the account's owner
     * @return the created Account
     */
    @Override
    public Account createAccount(String ownerLogin) {
        var account = new Account(nextId(), ownerLogin);
        accounts.put(account.getId(), account);
        return account;
    }

    /**
     * Retrieves an account by its unique ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return an {@code Optional} containing the account if found, otherwise an empty {@code Optional}
     */
    @Override
    public Optional<Account> findAccountById(long accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    /**
     * Retrieves all accounts associated with a specific user.
     *
     * @param ownerLogin the login of the account's owner
     * @return a {@code List} of all accounts belonging to the user
     */
    @Override
    public List<Account> findAllUserAccounts(String ownerLogin) {
        return accounts.values().stream()
                .filter(account -> account.getOwnerLogin().equals(ownerLogin))
                .toList();
    }

    /**
     * Generates a new unique account ID.
     *
     * @return a unique account ID
     */
    private long nextId() {
        return idCounter++;
    }
}
