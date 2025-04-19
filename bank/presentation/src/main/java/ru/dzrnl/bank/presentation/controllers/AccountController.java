package ru.dzrnl.bank.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzrnl.bank.business.contracts.AccountService;
import ru.dzrnl.bank.presentation.dto.AccountDto;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully created")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(@RequestBody String ownerLogin) {
        var createdAccount = accountService.createAccount(ownerLogin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AccountDto.fromDomain(createdAccount));
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

    @Operation(summary = "Get all accounts of a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User accounts retrieved successfully")
    })
    @GetMapping("/user/{userLogin}")
    public List<AccountDto> getAccountsByUserId(@PathVariable String userLogin) {
        return accountService.getAllUserAccounts(userLogin).stream()
                .map(AccountDto::fromDomain)
                .toList();
    }

    @Operation(summary = "Deposit money into an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Void> deposit(@PathVariable long accountId, @RequestParam long amount) {
        try {
            accountService.depositMoney(accountId, amount);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Withdraw money from an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal successful"),
            @ApiResponse(responseCode = "400", description = "Insufficient funds or invalid request"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<AccountDto> withdraw(@PathVariable long accountId, @RequestParam long amount) {
        try {
            accountService.withdrawMoney(accountId, amount);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Transfer money between accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Insufficient funds or invalid request"),
            @ApiResponse(responseCode = "404", description = "One or both accounts not found")
    })
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestParam long fromAccountId,
                                         @RequestParam long toAccountId,
                                         @RequestParam long amount) {
        try {
            accountService.transferMoney(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
