package ru.dzrnl.apigateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.dzrnl.apigateway.entities.CustomUserDetails;
import ru.dzrnl.apigateway.utils.ProxyUtil;

import java.util.Objects;

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
    @PreAuthorize("#userLogin == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<String> getAccountsByUser(@PathVariable String userLogin, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/accounts/user/" + userLogin, HttpMethod.GET, null);
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getAccountById(@PathVariable Long accountId, HttpServletRequest request, Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return proxyUtil.forwardRequest(request, "/api/accounts/" + accountId, HttpMethod.GET, null);
        }

        String userLogin = ((CustomUserDetails) authentication.getPrincipal()).getUsername();

        try {
            String accountJson = getAccountIfOwner(accountId, userLogin, request);

            if (accountJson == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(accountJson);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
        }
    }

    @GetMapping("/{accountId}/transactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getAccountTransactions(@PathVariable Long accountId, HttpServletRequest request, Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return proxyUtil.forwardRequest(request, "/api/transactions?accountId=" + accountId, HttpMethod.GET, null);
        }

        String userLogin = ((CustomUserDetails) authentication.getPrincipal()).getUsername();

        try {
            String accountJson = getAccountIfOwner(accountId, userLogin, request);

            if (accountJson == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return proxyUtil.forwardRequest(request, "/api/transactions?accountId=" + accountId, HttpMethod.GET, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<String> createAccount(HttpServletRequest request, Authentication authentication) {
        String userLogin = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        return proxyUtil.forwardRequestContentTypeJson(request, "/api/accounts", HttpMethod.POST, userLogin);
    }

    @PostMapping("/{accountId}/deposit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deposit(@PathVariable Long accountId,
                                          @RequestParam long amount,
                                          HttpServletRequest request,
                                          Authentication authentication) {
        String userLogin = ((CustomUserDetails) authentication.getPrincipal()).getUsername();

        try {
            String accountJson = getAccountIfOwner(accountId, userLogin, request);

            if (accountJson == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return proxyUtil.forwardRequest(request, "/api/accounts/" + accountId + "/deposit", HttpMethod.POST, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
        }
    }

    @PostMapping("/{accountId}/withdraw")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> withdraw(@PathVariable Long accountId,
                                           @RequestParam long amount,
                                           HttpServletRequest request,
                                           Authentication authentication) {
        String userLogin = ((CustomUserDetails) authentication.getPrincipal()).getUsername();

        try {
            String accountJson = getAccountIfOwner(accountId, userLogin, request);

            if (accountJson == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return proxyUtil.forwardRequest(request, "/api/accounts/" + accountId + "/withdraw", HttpMethod.POST, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
        }
    }

    @PostMapping("/transfer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> transfer(@RequestBody JsonNode transferDto, HttpServletRequest request, Authentication authentication) {
        long fromAccountId;

        if (transferDto.has("fromAccountId")) {
            fromAccountId = transferDto.get("fromAccountId").asLong();
        } else {
            return ResponseEntity.badRequest().body("Missing field: fromAccountId");
        }

        String userLogin = ((CustomUserDetails) authentication.getPrincipal()).getUsername();

        try {
            String accountJson = getAccountIfOwner(fromAccountId, userLogin, request);

            if (accountJson == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return proxyUtil.forwardRequest(request, "/api/accounts/transfer", HttpMethod.POST, transferDto.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
        }
    }

    private String getAccountIfOwner(Long accountId, String userLogin, HttpServletRequest request) throws JsonProcessingException {
        ResponseEntity<String> response = proxyUtil.forwardRequest(request, "/api/accounts/" + accountId, HttpMethod.GET, null);

        if (!response.getStatusCode().is2xxSuccessful()) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode accountNode = mapper.readTree(response.getBody());

        if (accountNode.has("ownerLogin") && Objects.equals(accountNode.get("ownerLogin").asText(), userLogin)) {
            return response.getBody();
        }

        return null;
    }
}
