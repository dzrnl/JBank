package ru.dzrnl.bank.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreatedEventDto {
    private Long userId;
    private String login;
    private String name;
    private Integer age;
    private Gender gender;
    private HairColor hairColor;
}
