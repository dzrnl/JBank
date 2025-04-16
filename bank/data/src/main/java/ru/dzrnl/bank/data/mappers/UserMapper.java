package ru.dzrnl.bank.data.mappers;

import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.data.entities.UserEntity;

public class UserMapper {
    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .login(user.getLogin())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .hairColor(user.getHairColor())
                .build();
    }

    public static User toDomain(UserEntity user) {
        return User.builder()
                .login(user.getLogin())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .hairColor(user.getHairColor())
                .build();
    }
}
