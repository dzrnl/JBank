package ru.dzrnl.apigateway.business.dto.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private long id;
    private long accountId;
    private long amount;
    private TransactionType type;
}
