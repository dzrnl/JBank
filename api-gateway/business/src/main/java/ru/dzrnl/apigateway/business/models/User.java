package ru.dzrnl.apigateway.business.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    private String login;
    private String passwordHash;
    private Role role;
}
