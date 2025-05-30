package ru.dzrnl.bank.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dzrnl.bank.business.contracts.AccountService;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.account.TransactionType;
import ru.dzrnl.bank.presentation.dto.TransactionDto;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/transactions")
public class TransactionController {
    private final AccountService accountService;

    public TransactionController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Get all transactions",
            description = "Retrieve all transactions with optional filtering by transaction type and account ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping
    public List<TransactionDto> getAllTransactions(@RequestParam(required = false) String type,
                                                   @RequestParam(required = false) Long accountId) {
        TransactionType typeEnum = type != null ? TransactionType.valueOf(type.toUpperCase()) : null;

        Set<Transaction> result;
        
        if (type != null && accountId != null) {
            result = accountService.getAllTransactionsFilteredByAccountAndType(accountId, typeEnum);
        } else if (accountId != null) {
            result = accountService.getAllTransactionsFilteredByAccount(accountId);
        } else if (type != null) {
            result = accountService.getAllTransactionsFilteredByType(typeEnum);
        } else {
            result = accountService.getAllTransactions();
        }

        return result.stream()
                .map(TransactionDto::fromDomain)
                .toList();
    }
}
