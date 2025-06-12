package ru.dzrnl.apigateway.data.repositories;

import org.springframework.stereotype.Repository;
import ru.dzrnl.apigateway.business.models.User;
import ru.dzrnl.apigateway.business.repositories.UserRepository;
import ru.dzrnl.apigateway.data.entities.UserEntity;
import ru.dzrnl.apigateway.data.mappers.UserMapper;
import ru.dzrnl.apigateway.data.repositories.jpa.UserJpaRepository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        userJpaRepository.save(userEntity);
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userJpaRepository.findByLogin(login).map(UserMapper::toDomain);
    }
}
