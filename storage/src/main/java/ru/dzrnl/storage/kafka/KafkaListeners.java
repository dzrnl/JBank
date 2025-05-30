package ru.dzrnl.storage.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.dzrnl.storage.services.EventStorageService;

@Service
public class KafkaListeners {
    private final EventStorageService storageService;

    public KafkaListeners(EventStorageService storageService) {
        this.storageService = storageService;
    }

    @KafkaListener(topics = "client-topic", groupId = "storage-group")
    public void consumeClientEvent(@Payload String message,
                                   @Header(KafkaHeaders.RECEIVED_KEY) String key,
                                   @Header("eventType") String eventType) {
        long userId = Long.parseLong(key);
        if (eventType.equals("USER_CREATED")) {
            storageService.saveUserEvent(userId, message);
        } else {
            System.err.println("Unknown eventType: " + eventType);
        }
    }

    @KafkaListener(topics = "account-topic", groupId = "storage-group")
    public void consumeAccountEvent(@Payload String message,
                                    @Header(KafkaHeaders.RECEIVED_KEY) String key,
                                    @Header("eventType") String eventType) {
        long accountId = Long.parseLong(key);
        switch (eventType) {
            case "ACCOUNT_CREATED" -> storageService.saveAccountEvent(accountId, message);
            case "TRANSACTION" -> storageService.saveTransactionEvent(accountId, message);
            default -> System.err.println("Unknown eventType: " + eventType);
        }
    }
}
