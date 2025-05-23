package ru.dzrnl.apigateway.business.contracts;

import ru.dzrnl.apigateway.business.dto.accounts.AccountDetailsDto;
import ru.dzrnl.apigateway.business.dto.accounts.AccountDto;
import ru.dzrnl.apigateway.business.dto.accounts.TransactionDto;

import java.util.List;

public interface AccountService {
    AccountDto createAccount(String ownerLogin);

    List<AccountDto> getAllAccounts();

    AccountDto getAccount(Long accountId);

    List<AccountDto> getAllUserAccounts(String userLogin);

    void depositMoney(Long accountId, Long amount);

    void withdrawMoney(Long accountId, Long amount);

    void transferMoney(Long fromAccountId, Long toAccountId, Long amount);

    List<TransactionDto> getAllTransactions();

    AccountDetailsDto getAccountWithTransactions(Long accountId);
}
