package ru.dzrnl.bank.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dzrnl.bank.business.models.account.Account;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {
    private Long id;
    private long balance;
    private String ownerLogin;

    public static AccountDto fromDomain(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .balance(account.getBalance())
                .ownerLogin(account.getOwnerLogin())
                .build();
    }
}
