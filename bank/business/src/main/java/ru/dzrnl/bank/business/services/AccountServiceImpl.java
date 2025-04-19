package ru.dzrnl.bank.business.services;

import org.springframework.stereotype.Service;
import ru.dzrnl.bank.business.contracts.AccountService;
import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;
import ru.dzrnl.bank.business.repositories.AccountRepository;
import ru.dzrnl.bank.business.repositories.TransactionRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link AccountService} for managing accounts.
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FriendshipService friendshipService;

    /**
     * Creates a new instance of {@code AccountServiceImpl} with required dependencies.
     *
     * @param accountRepository     the repository for managing accounts
     * @param transactionRepository the repository for managing transactions
     * @param friendshipService     the service for checking friendship status between users
     */
    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, FriendshipService friendshipService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.friendshipService = friendshipService;
    }

    /**
     * Creates a new account for the specified user.
     *
     * @param userLogin the login of the user for whom the account will be created
     * @return the created account
     */
    @Override
    public Account createAccount(String userLogin) {
        var account = new Account(userLogin);

        return accountRepository.save(account);
    }

    /**
     * Retrieves an account by its unique ID.
     *
     * @param accountId the ID of the account to retrieve
     * @return the account corresponding to the provided ID
     * @throws IllegalArgumentException if no account with the given ID is found
     */
    @Override
    public Account getAccount(long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id '" + accountId + "' not found"));
    }

    /**
     * Retrieves all accounts.
     *
     * @return a {@code Set} of all accounts
     */
    @Override
    public Set<Account> getAllAccounts() {
        return new HashSet<>(accountRepository.findAll());
    }

    /**
     * Retrieves all accounts belonging to a specific user.
     *
     * @param userLogin the login of the user whose accounts will be retrieved
     * @return a {@code Set} of accounts associated with the provided user login
     */
    @Override
    public Set<Account> getAllUserAccounts(String userLogin) {
        return new HashSet<>(accountRepository.findAllByOwnerLogin(userLogin));
    }

    /**
     * Withdraws money from the account.
     *
     * @param accountId the ID of the account to withdraw money from
     * @param amount    the amount of money to withdraw
     * @throws IllegalArgumentException if no account with the given ID is found
     * @throws IllegalStateException    if a withdrawal is attempted with insufficient funds
     */
    @Override
    public void withdrawMoney(long accountId, long amount) {
        Account account = getAccount(accountId);

        Transaction transaction = transactionRepository.save(
                accountId,
                amount,
                TransactionType.WITHDRAW
        );

        try {
            executeTransaction(transaction, account);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Insufficient funds for account " + accountId);
        }
    }

    /**
     * Deposits money into the account.
     *
     * @param accountId the ID of the account to deposit money into
     * @param amount    the amount of money to deposit
     * @throws IllegalArgumentException if no account with the given ID is found
     */
    @Override
    public void depositMoney(long accountId, long amount) {
        Account account = getAccount(accountId);

        Transaction transaction = transactionRepository.save(
                accountId,
                amount,
                TransactionType.DEPOSIT
        );

        executeTransaction(transaction, account);
    }

    /**
     * Transfers money from one account to another, applying different fees based on user relationships.
     *
     * @param fromAccountId the ID of the account from which the money is being transferred
     * @param toAccountId   the ID of the account to which the money is being transferred
     * @param amount        the amount of money to transfer
     * @throws IllegalArgumentException if no account with the given ID is found
     * @throws IllegalStateException    if a withdrawal is attempted with insufficient funds
     */
    @Override
    public void transferMoney(long fromAccountId, long toAccountId, long amount) {
        var fromAccount = getAccount(fromAccountId);
        var toAccount = getAccount(toAccountId);

        Transaction fromTransaction = transactionRepository.save(
                fromAccountId,
                amount,
                TransactionType.WITHDRAW
        );

        try {
            executeTransaction(fromTransaction, fromAccount);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Insufficient funds for account " + fromAccountId);
        }

        amount = calculateTransferAmountWithFee(fromAccount, toAccount, amount);

        Transaction toTransaction = transactionRepository.save(
                toAccountId,
                amount,
                TransactionType.DEPOSIT
        );

        executeTransaction(toTransaction, toAccount);
    }

    /**
     * Retrieves all transactions.
     *
     * @return a {@code Set} of all transactions
     */
    @Override
    public Set<Transaction> getAllTransactions() {
        return new HashSet<>(transactionRepository.findAll());
    }

    @Override
    public Set<Transaction> getAllTransactionsFilteredByAccount(long accountId) {
        return getAllTransactions().stream()
                .filter(transaction -> transaction.accountId() == accountId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Transaction> getAllTransactionsFilteredByType(TransactionType type) {
        return getAllTransactions().stream()
                .filter(transaction -> transaction.type() == type)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Transaction> getAllTransactionsFilteredByAccountAndType(long accountId, TransactionType type) {
        return getAllTransactions().stream()
                .filter(transaction -> transaction.accountId() == accountId
                        && transaction.type() == type)
                .collect(Collectors.toSet());
    }

    /**
     * Calculates the transfer amount with the appropriate fee based on user relationships.
     * - No fee for transfers between the same user's accounts.
     * - Friends pay a 3% fee.
     * - Non-friends pay a 10% fee.
     *
     * @param fromAccount the account from which money is being transferred
     * @param toAccount   the account to which money is being transferred
     * @param amount      the original transfer amount
     * @return the transfer amount after applying the fee
     */
    private long calculateTransferAmountWithFee(Account fromAccount, Account toAccount, long amount) {
        var fromUser = fromAccount.getOwnerLogin();
        var toUser = toAccount.getOwnerLogin();

        if (fromUser.equals(toUser)) {
            return amount;
        } else if (friendshipService.areFriends(fromUser, toUser)) {
            return amount - amount * 3 / 100;
        } else {
            return amount - amount * 10 / 100;
        }
    }

    /**
     * Executes a transaction and updates the associated account.
     *
     * @param transaction the transaction to execute
     * @param account     the account that will be updated after the transaction is executed
     */
    private void executeTransaction(Transaction transaction, Account account) {
        transaction.execute(account);
        accountRepository.save(account);
    }
}
