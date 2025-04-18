package ru.dzrnl.bank.data.mappers;

import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.data.entities.AccountEntity;
import ru.dzrnl.bank.data.entities.TransactionEntity;

public class TransactionMapper {
    public static TransactionEntity toEntity(Transaction transaction, AccountEntity account) {
        return TransactionEntity.builder()
                .id(transaction.id())
                .account(account)
                .amount(transaction.amount())
                .type(transaction.type())
                .build();
    }

    public static Transaction toDomain(TransactionEntity transaction) {
        return new Transaction(transaction.getId(),
                transaction.getAccount().getId(),
                transaction.getAmount(),
                transaction.getType());
    }
}
