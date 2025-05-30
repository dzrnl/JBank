package ru.dzrnl.bank.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dzrnl.bank.business.models.account.TransactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEventDto {
    private Long transactionId;
    private Long accountId;
    private Long amount;
    private TransactionType type;
}
