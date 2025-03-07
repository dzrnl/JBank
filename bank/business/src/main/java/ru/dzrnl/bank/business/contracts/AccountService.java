package ru.dzrnl.bank.business.contracts;

import ru.dzrnl.bank.business.models.account.Account;

import java.util.Set;

/**
 * Provides operations related to account management.
 */
public interface AccountService {

    /**
     * Creates a new account for the specified user.
     *
     * @param userLogin the login of the user for whom the account will be created
     * @return the created Account
     */
    Account createAccount(String userLogin);

    /**
     * Retrieves an account by its unique ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return the account corresponding to the provided ID
     * @throws IllegalArgumentException if no account with the given ID is found
     */
    Account getAccount(long accountId);

    /**
     * Retrieves all accounts belonging to a specific user.
     *
     * @param userLogin the login of the user whose accounts will be retrieved
     * @return a {@code Set} of accounts associated with the provided user login
     */
    Set<Account> getAllUserAccounts(String userLogin);

    /**
     * Withdraws money from the account.
     *
     * @param accountId the ID of the account to withdraw money from
     * @param amount    the amount of money to withdraw
     */
    void withdrawMoney(long accountId, long amount);

    /**
     * Deposits money into the account.
     *
     * @param accountId the ID of the account to deposit money into
     * @param amount    the amount of money to deposit
     */
    void depositMoney(long accountId, long amount);

    /**
     * Transfers money from one account to another, applying different fees based on user relationships.
     *
     * @param fromAccountId the ID of the account from which the money is being transferred
     * @param toAccountId   the ID of the account to which the money is being transferred
     * @param amount        the amount of money to transfer
     */
    void transferMoney(long fromAccountId, long toAccountId, long amount);
}
