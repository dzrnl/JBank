package ru.dzrnl.apigateway.business.dto.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDetailsDto {
    private AccountDto account;
    private List<TransactionDto> transactions;
}
