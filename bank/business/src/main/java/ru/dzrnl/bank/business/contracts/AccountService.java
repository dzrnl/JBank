package ru.dzrnl.bank.business.contracts;

import ru.dzrnl.bank.business.models.account.Account;

import java.util.Set;

public interface AccountService {
    Account createAccount(String userLogin);

    Account getAccount(long accountId);

    Set<Account> getAllUserAccounts(String userLogin);

    void withdrawMoney(long accountId, long amount);

    void depositMoney(long accountId, long amount);

    void transferMoney(long fromAccountId, long toAccountId, long amount);
}
