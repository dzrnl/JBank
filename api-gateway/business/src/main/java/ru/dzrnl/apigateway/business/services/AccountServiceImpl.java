package ru.dzrnl.apigateway.business.services;

import org.springframework.stereotype.Service;
import ru.dzrnl.apigateway.business.clients.AccountClient;
import ru.dzrnl.apigateway.business.contracts.AccountService;
import ru.dzrnl.apigateway.business.dto.accounts.AccountDetailsDto;
import ru.dzrnl.apigateway.business.dto.accounts.AccountDto;
import ru.dzrnl.apigateway.business.dto.accounts.TransactionDto;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountClient accountClient;

    public AccountServiceImpl(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @Override
    public AccountDto createAccount(String ownerLogin) {
        return accountClient.createAccount(ownerLogin);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountClient.getAllAccounts();
    }

    @Override
    public AccountDto getAccount(Long accountId) {
        return accountClient.getAccount(accountId);
    }

    @Override
    public List<AccountDto> getAllUserAccounts(String userLogin) {
        return accountClient.getAllUserAccounts(userLogin);
    }

    @Override
    public void depositMoney(Long accountId, Long amount) {
        accountClient.depositMoney(accountId, amount);
    }

    @Override
    public void withdrawMoney(Long accountId, Long amount) {
        accountClient.withdrawMoney(accountId, amount);
    }

    @Override
    public void transferMoney(Long fromAccountId, Long toAccountId, Long amount) {
        accountClient.transferMoney(fromAccountId, toAccountId, amount);
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        return accountClient.getAllTransactions();
    }

    @Override
    public AccountDetailsDto getAccountWithTransactions(Long accountId) {
        AccountDto account = accountClient.getAccount(accountId);

        List<TransactionDto> transactions = accountClient.getAllTransactions().stream()
                .filter(tx -> tx.getAccountId() == accountId)
                .toList();

        return new AccountDetailsDto(account, transactions);
    }
}
