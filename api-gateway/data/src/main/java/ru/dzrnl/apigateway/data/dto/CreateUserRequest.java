package ru.dzrnl.apigateway.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dzrnl.apigateway.business.dto.users.Gender;
import ru.dzrnl.apigateway.business.dto.users.HairColor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String login;
    private String name;
    private int age;
    private Gender gender;
    private HairColor hairColor;
}
