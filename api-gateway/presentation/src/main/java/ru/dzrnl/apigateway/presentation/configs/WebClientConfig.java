package ru.dzrnl.apigateway.presentation.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${bank-app.base-url}")
    private String bankUrl;

    @Bean
    public WebClient userWebClient() {
        return WebClient.builder()
                .baseUrl(bankUrl)
                .build();
    }
}
