package ru.dzrnl.bank.presentation.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dzrnl.bank.business.contracts.AccountService;
import ru.dzrnl.bank.presentation.dto.AccountDto;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountDto> getAllAccounts() {
        return accountService.getAllAccounts().stream()
                .map(AccountDto::fromDomain)
                .toList();
    }
}
