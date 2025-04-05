package ru.dzrnl.bank.data.mappers;

import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.data.entities.UserEntity;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .login(user.login())
                .name(user.name())
                .age(user.age())
                .gender(user.gender())
                .hairColor(user.hairColor())
                .build();
    }

    public static User toDomain(UserEntity user) {
        return new User(user.getLogin(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHairColor());
    }
}
