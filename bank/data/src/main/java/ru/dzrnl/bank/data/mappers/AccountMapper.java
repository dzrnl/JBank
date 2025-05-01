package ru.dzrnl.bank.data.mappers;

import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.data.entities.AccountEntity;
import ru.dzrnl.bank.data.entities.UserEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AccountMapper {
    public static AccountEntity toEntity(Account account, UserEntity owner) {
        AccountEntity.AccountEntityBuilder builder = AccountEntity.builder()
                .owner(owner)
                .balance(account.getBalance());

        if (account.getId() != null) {
            builder.id(account.getId());
        }

        AccountEntity accountEntity = builder.build();

        if (account.getTransactionHistory() != null) {
            accountEntity.setTransactionHistory(account.getTransactionHistory().stream()
                    .map(transaction -> TransactionMapper.toEntity(transaction, accountEntity))
                    .toList());
        }

        return accountEntity;
    }

    public static Account toDomain(AccountEntity account) {
        ArrayList<Transaction> transactionHistory = null;
        if (account.getTransactionHistory() != null) {
            transactionHistory = account.getTransactionHistory().stream()
                    .map(TransactionMapper::toDomain)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return new Account(account.getId(),
                account.getBalance(),
                account.getOwner().getLogin(),
                transactionHistory);
    }
}
