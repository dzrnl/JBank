package ru.dzrnl.storage.services;

import org.springframework.stereotype.Service;
import ru.dzrnl.storage.entities.AccountEvent;
import ru.dzrnl.storage.entities.TransactionEvent;
import ru.dzrnl.storage.entities.UserEvent;
import ru.dzrnl.storage.repositories.AccountEventRepository;
import ru.dzrnl.storage.repositories.TransactionEventRepository;
import ru.dzrnl.storage.repositories.UserEventRepository;

@Service
public class EventStorageService {
    private final AccountEventRepository accountEventRepository;
    private final TransactionEventRepository transactionEventRepository;
    private final UserEventRepository userEventRepository;

    public EventStorageService(AccountEventRepository accountEventRepository, TransactionEventRepository transactionEventRepository, UserEventRepository userEventRepository) {
        this.accountEventRepository = accountEventRepository;
        this.transactionEventRepository = transactionEventRepository;
        this.userEventRepository = userEventRepository;
    }

    public void saveAccountEvent(Long accountId, String json) {
        AccountEvent accountEvent = AccountEvent.builder()
                .accountId(accountId)
                .eventJson(json)
                .build();
        accountEventRepository.save(accountEvent);
    }

    public void saveTransactionEvent(Long accountId, String json) {
        TransactionEvent transactionEvent = TransactionEvent.builder()
                .accountId(accountId)
                .eventJson(json)
                .build();
        transactionEventRepository.save(transactionEvent);
    }

    public void saveUserEvent(Long userId, String json) {
        UserEvent userEvent = UserEvent.builder()
                .userId(userId)
                .eventJson(json)
                .build();
        userEventRepository.save(userEvent);
    }
}
