package ru.dzrnl.bank.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    })
    @GetMapping
    public List<AccountDto> getAllAccounts() {
        return accountService.getAllAccounts().stream()
                .map(AccountDto::fromDomain)
                .toList();
    }
}
