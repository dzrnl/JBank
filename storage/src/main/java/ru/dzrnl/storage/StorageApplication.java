package ru.dzrnl.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.dzrnl.storage")
@EnableJpaRepositories(basePackages = "ru.dzrnl.storage.repositories")
@EntityScan(basePackages = "ru.dzrnl.storage.entities")
public class StorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(StorageApplication.class, args);
    }
}
