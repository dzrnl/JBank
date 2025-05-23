package ru.dzrnl.apigateway.presentation.dto;

import lombok.Data;
import ru.dzrnl.apigateway.business.dto.Gender;
import ru.dzrnl.apigateway.business.dto.HairColor;

@Data
public class CreateUserDto {
    private String login;
    private String password;
    private String name;
    private int age;
    private Gender gender;
    private HairColor hairColor;
}
