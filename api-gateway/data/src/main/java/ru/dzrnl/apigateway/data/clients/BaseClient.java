package ru.dzrnl.apigateway.data.clients;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class BaseClient {
    protected final WebClient webClient;

    protected BaseClient(WebClient webClient) {
        this.webClient = webClient;
    }

    protected <T> T handleResponse(WebClient.ResponseSpec responseSpec, Class<T> clazz) {
        return responseSpec
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new IllegalArgumentException("Bad request: " + errorBody))))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Server error: " + errorBody))))
                .onStatus(status -> !status.is2xxSuccessful(), clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Unexpected status code "
                                        + clientResponse.statusCode() + ": " + errorBody))))
                .bodyToMono(clazz)
                .block();
    }

    protected void handleEmptyResponse(WebClient.ResponseSpec responseSpec) {
        handleResponse(responseSpec, Void.class);
    }
}
