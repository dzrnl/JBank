package ru.dzrnl.bank.data.repositories;

import org.springframework.stereotype.Repository;
import ru.dzrnl.bank.business.models.account.Account;
import ru.dzrnl.bank.business.repositories.AccountRepository;
import ru.dzrnl.bank.data.entities.AccountEntity;
import ru.dzrnl.bank.data.entities.UserEntity;
import ru.dzrnl.bank.data.mappers.AccountMapper;
import ru.dzrnl.bank.data.repositories.jpa.AccountJpaRepository;
import ru.dzrnl.bank.data.repositories.jpa.UserJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final AccountJpaRepository accountJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public AccountRepositoryImpl(AccountJpaRepository accountJpaRepository, UserJpaRepository userJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Account save(Account account) {
        UserEntity owner = userJpaRepository.findByLogin(account.getOwnerLogin())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + account.getOwnerLogin()));

        AccountEntity entity = AccountMapper.toEntity(account, owner);
        AccountEntity saved = accountJpaRepository.save(entity);
        return AccountMapper.toDomain(saved);
    }

    @Override
    public Optional<Account> findById(long accountId) {
        return accountJpaRepository.findById(accountId).map(AccountMapper::toDomain);
    }

    @Override
    public List<Account> findAllByOwnerLogin(String ownerLogin) {
        return accountJpaRepository.findAllByOwner_Login(ownerLogin).stream()
                .map(AccountMapper::toDomain)
                .toList();
    }
}
