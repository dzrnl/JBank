package ru.dzrnl.bank.business.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import ru.dzrnl.bank.business.dto.AccountCreatedEventDto;
import ru.dzrnl.bank.business.dto.TransactionEventDto;
import ru.dzrnl.bank.business.dto.UserCreatedEventDto;
import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.models.account.Transaction;
import ru.dzrnl.bank.business.models.user.User;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishUserCreatedEvent(User user) {
        try {
            UserCreatedEventDto dto = UserCreatedEventDto.builder()
                    .userId(user.getId())
                    .login(user.getLogin())
                    .name(user.getName())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .hairColor(user.getHairColor())
                    .build();

            String payload = objectMapper.writeValueAsString(dto);
            Message<String> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, "client-topic")
                    .setHeader(KafkaHeaders.KEY, user.getId().toString())
                    .setHeader("eventType", "USER_CREATED")
                    .build();

            kafkaTemplate.send(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize user event", e);
        }
    }

    public void publishAccountCreatedEvent(Account account) {
        try {
            AccountCreatedEventDto dto = AccountCreatedEventDto.builder()
                    .accountId(account.getId())
                    .ownerLogin(account.getOwnerLogin())
                    .build();

            String payload = objectMapper.writeValueAsString(dto);
            Message<String> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, "account-topic")
                    .setHeader(KafkaHeaders.KEY, account.getId().toString())
                    .setHeader("eventType", "ACCOUNT_CREATED")
                    .build();

            kafkaTemplate.send(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize account created event", e);
        }
    }

    public void publishTransactionEvent(Transaction transaction) {
        try {
            TransactionEventDto dto = TransactionEventDto.builder()
                    .transactionId(transaction.id())
                    .accountId(transaction.accountId())
                    .amount(transaction.amount())
                    .type(transaction.type())
                    .build();

            String payload = objectMapper.writeValueAsString(dto);
            Message<String> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, "account-topic")
                    .setHeader(KafkaHeaders.KEY, String.valueOf(transaction.accountId()))
                    .setHeader("eventType", "TRANSACTION")
                    .build();

            kafkaTemplate.send(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize transaction event", e);
        }
    }
}
