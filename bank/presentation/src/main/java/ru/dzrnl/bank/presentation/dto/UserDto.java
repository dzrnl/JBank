package ru.dzrnl.bank.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String login;
    private String name;
    private int age;
    private Gender gender;
    private HairColor hairColor;

    public static UserDto fromDomain(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .hairColor(user.getHairColor())
                .build();
    }
}
