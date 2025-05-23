package ru.dzrnl.apigateway.data.mappers;

import ru.dzrnl.apigateway.business.models.User;
import ru.dzrnl.apigateway.data.entities.UserEntity;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .login(user.getLogin())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .build();
    }

    public static User toDomain(UserEntity user) {
        return User.builder()
                .id(user.getId())
                .login(user.getLogin())
                .passwordHash(user.getPasswordHash())
                .role(user.getRole())
                .build();
    }
}
