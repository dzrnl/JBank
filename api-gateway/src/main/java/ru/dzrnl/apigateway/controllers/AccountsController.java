package ru.dzrnl.apigateway.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.dzrnl.apigateway.utils.ProxyUtil;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
    private final ProxyUtil proxyUtil;

    public AccountsController(ProxyUtil proxyUtil) {
        this.proxyUtil = proxyUtil;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAllAccounts(HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/accounts", HttpMethod.GET, null);
    }

    @GetMapping("/user/{userLogin}")
    @PreAuthorize("#userLogin == authentication.principal.login or hasRole('ADMIN')") //
    public ResponseEntity<String> getAccountsByUser(@PathVariable String userLogin, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/accounts/user/" + userLogin, HttpMethod.GET, null);
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("hasRole('ADMIN')") //
    public ResponseEntity<String> getAccountById(@PathVariable Long accountId, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/accounts/" + accountId, HttpMethod.GET, null);
    }

    @GetMapping("/{accountId}/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAccountTransactions(@PathVariable Long accountId, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/transactions?accountId=" + accountId, HttpMethod.GET, null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createAccount(@RequestBody String ownerLogin, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/accounts", HttpMethod.POST, ownerLogin);
    }

    @PostMapping("/{accountId}/deposit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deposit(@PathVariable Long accountId,
                                          @RequestParam long amount,
                                          HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/accounts/" + accountId + "/deposit?amount=" + amount, HttpMethod.POST, null);
    }

    @PostMapping("/{accountId}/withdraw")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> withdraw(@PathVariable Long accountId,
                                           @RequestParam long amount,
                                           HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/accounts/" + accountId + "/withdraw?amount=" + amount, HttpMethod.POST, null);
    }

    @PostMapping("/transfer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> transfer(@RequestBody JsonNode transferDto, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/accounts/transfer", HttpMethod.POST, transferDto.toString());
    }
}
