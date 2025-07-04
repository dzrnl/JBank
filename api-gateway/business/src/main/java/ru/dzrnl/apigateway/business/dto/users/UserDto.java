package ru.dzrnl.apigateway.business.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
