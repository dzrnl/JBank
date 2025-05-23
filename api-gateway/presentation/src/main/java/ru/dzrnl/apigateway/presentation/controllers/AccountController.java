package ru.dzrnl.apigateway.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.dzrnl.apigateway.business.contracts.AccountService;
import ru.dzrnl.apigateway.business.dto.accounts.AccountDetailsDto;
import ru.dzrnl.apigateway.business.dto.accounts.AccountDto;
import ru.dzrnl.apigateway.business.dto.accounts.TransactionDto;
import ru.dzrnl.apigateway.presentation.dto.TransferDto;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AccountDto> createAccount(@AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        AccountDto accountDto = accountService.createAccount(login);
        return ResponseEntity.ok().body(accountDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountDto> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/user/{userLogin}")
    @PreAuthorize("hasRole('ADMIN') or #userLogin == authentication.name")
    public List<AccountDto> getAccountsByUserLogin(@PathVariable String userLogin) {
        return accountService.getAllUserAccounts(userLogin);
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isAccountOwner(#accountId, authentication.name)")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long accountId) {
        AccountDto accountDto = accountService.getAccount(accountId);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/{accountId}/details")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isAccountOwner(#accountId, authentication.name)")
    public ResponseEntity<AccountDetailsDto> getAccountWithTransactions(@PathVariable Long accountId) {
        AccountDetailsDto accountDto = accountService.getAccountWithTransactions(accountId);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/{accountId}/deposit")
    @PreAuthorize("@userSecurity.isAccountOwner(#accountId, authentication.name)")
    public ResponseEntity<Void> deposit(@PathVariable Long accountId, @RequestParam Long amount) {
        accountService.depositMoney(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{accountId}/withdraw")
    @PreAuthorize("@userSecurity.isAccountOwner(#accountId, authentication.name)")
    public ResponseEntity<Void> withdraw(@PathVariable Long accountId, @RequestParam Long amount) {
        accountService.withdrawMoney(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    @PreAuthorize("@userSecurity.isAccountOwner(#transferDto.fromAccountId, authentication.name)")
    public ResponseEntity<Void> transfer(@RequestBody TransferDto transferDto) {
        accountService.transferMoney(
                transferDto.getFromAccountId(),
                transferDto.getToAccountId(),
                transferDto.getAmount()
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionDto> getAllTransactions() {
        return accountService.getAllTransactions();
    }
}
