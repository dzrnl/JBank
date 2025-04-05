package ru.dzrnl.bank.business.repositories;

import ru.dzrnl.bank.business.models.account.Account;

import java.util.List;
import java.util.Optional;

/**
 * Provides operations for managing accounts in a repository.
 */
public interface AccountRepository {

    /**
     * Creates a new account for a user.
     *
     * @param ownerLogin the login of the account's owner
     * @return the created account
     */
    Account createAccount(String ownerLogin);

    /**
     * Updates the details of an existing account.
     *
     * @param account the account to update, containing the updated information
     */
    void updateAccount(Account account);

    /**
     * Retrieves an account by its unique ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return an {@code Optional} containing the account if found, otherwise an empty {@code Optional}
     */
    Optional<Account> findAccountById(long accountId);

    /**
     * Retrieves all accounts associated with a specific user.
     *
     * @param ownerLogin the login of the account's owner
     * @return a {@code List} of all accounts belonging to the user
     */
    List<Account> findAllUserAccounts(String ownerLogin);
}
