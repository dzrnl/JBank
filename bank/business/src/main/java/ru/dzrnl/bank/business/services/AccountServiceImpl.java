package ru.dzrnl.bank.business.services;

import ru.dzrnl.bank.business.contracts.AccountService;
import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;
import ru.dzrnl.bank.business.repositories.AccountRepository;
import ru.dzrnl.bank.business.repositories.TransactionRepository;

import java.util.Set;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final FriendshipService friendshipService;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, FriendshipService friendshipService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.friendshipService = friendshipService;
    }

    @Override
    public Account createAccount(String userLogin) {
        return accountRepository.createAccount(userLogin);
    }

    @Override
    public Account getAccount(long accountId) {
        return accountRepository.findAccountById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account with id '" + accountId + "' not found"));
    }

    @Override
    public Set<Account> getAllUserAccounts(String userLogin) {
        return accountRepository.findAllUserAccounts(userLogin);
    }

    @Override
    public void withdrawMoney(long accountId, long amount) {
        Account account = getAccount(accountId);

        Transaction transaction = transactionRepository.createTransaction(
                accountId,
                amount,
                TransactionType.WITHDRAW
        );

        transaction.execute(account);
    }

    @Override
    public void depositMoney(long accountId, long amount) {
        Account account = getAccount(accountId);

        Transaction transaction = transactionRepository.createTransaction(
                accountId,
                amount,
                TransactionType.DEPOSIT
        );

        transaction.execute(account);
    }

    @Override
    public void transferMoney(long fromAccountId, long toAccountId, long amount) {
        var fromAccount = getAccount(fromAccountId);
        var toAccount = getAccount(toAccountId);

        Transaction fromTransaction = transactionRepository.createTransaction(
                fromAccountId,
                amount,
                TransactionType.WITHDRAW
        );

        fromTransaction.execute(fromAccount);

        amount = calculateTransferAmountWithFee(fromAccount, toAccount, amount);

        Transaction toTransaction = transactionRepository.createTransaction(
                toAccountId,
                amount,
                TransactionType.DEPOSIT
        );

        toTransaction.execute(toAccount);
    }

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
}
