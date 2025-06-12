package ru.dzrnl.apigateway.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.dzrnl.apigateway")
@EnableJpaRepositories(basePackages = "ru.dzrnl.apigateway.data.repositories.jpa")
@EntityScan(basePackages = "ru.dzrnl.apigateway.data.entities")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
