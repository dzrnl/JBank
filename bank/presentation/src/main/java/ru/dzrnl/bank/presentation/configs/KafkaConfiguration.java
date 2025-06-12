package ru.dzrnl.bank.presentation.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {
    @Bean
    public NewTopic clientTopic() {
        return new NewTopic("client-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic accountTopic() {
        return new NewTopic("account-topic", 3, (short) 1);
    }
}
