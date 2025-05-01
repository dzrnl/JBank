package ru.dzrnl.bank.presentation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.dzrnl.bank")
@EnableJpaRepositories(basePackages = "ru.dzrnl.bank.data.repositories.jpa")
@EntityScan(basePackages = "ru.dzrnl.bank.data.entities")
public class BankApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }
}
