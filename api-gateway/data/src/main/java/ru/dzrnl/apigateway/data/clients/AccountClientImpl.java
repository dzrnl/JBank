package ru.dzrnl.apigateway.data.clients;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.dzrnl.apigateway.business.clients.AccountClient;
import ru.dzrnl.apigateway.business.dto.accounts.AccountDto;
import ru.dzrnl.apigateway.business.dto.accounts.TransactionDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class AccountClientImpl extends BaseClient implements AccountClient {
    public AccountClientImpl(WebClient webClient) {
        super(webClient);
    }

    @Override
    public AccountDto createAccount(String ownerLogin) {
        return handleResponse(
                webClient.post()
                        .uri("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ownerLogin)
                        .retrieve(),
                AccountDto.class
        );
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        AccountDto[] array = handleResponse(
                webClient.get()
                        .uri("/api/accounts")
                        .retrieve(),
                AccountDto[].class
        );

        return Arrays.asList(array);
    }

    @Override
    public AccountDto getAccount(Long accountId) {
        return handleResponse(
                webClient.get()
                        .uri("/api/accounts/{id}", accountId)
                        .retrieve(),
                AccountDto.class
        );
    }

    @Override
    public List<AccountDto> getAllUserAccounts(String userLogin) {
        AccountDto[] array = handleResponse(
                webClient.get()
                        .uri("/api/accounts/user/{login}", userLogin)
                        .retrieve(),
                AccountDto[].class
        );

        return Arrays.asList(array);
    }

    @Override
    public void depositMoney(Long accountId, Long amount) {
        handleEmptyResponse(
                webClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/accounts/{id}/deposit")
                                .queryParam("amount", amount)
                                .build(accountId))
                        .retrieve()
        );
    }

    @Override
    public void withdrawMoney(Long accountId, Long amount) {
        handleEmptyResponse(
                webClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/accounts/{id}/withdraw")
                                .queryParam("amount", amount)
                                .build(accountId))
                        .retrieve()
        );
    }

    @Override
    public void transferMoney(Long fromAccountId, Long toAccountId, Long amount) {
        handleEmptyResponse(
                webClient.post()
                        .uri("/api/accounts/transfer")
                        .bodyValue(Map.of(
                                "fromAccountId", fromAccountId,
                                "toAccountId", toAccountId,
                                "amount", amount
                        ))
                        .retrieve()
        );
    }

    @Override
    public List<TransactionDto> getAllTransactions() {
        TransactionDto[] array = handleResponse(
                webClient.get()
                        .uri("/api/transactions")
                        .retrieve(),
                TransactionDto[].class
        );

        return Arrays.asList(array);
    }
}
