package ru.dzrnl.bank.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private long id;
    private long accountId;
    private long amount;
    private TransactionType type;

    public static TransactionDto fromDomain(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.id())
                .accountId(transaction.accountId())
                .amount(transaction.amount())
                .type(transaction.type())
                .build();
    }
}
