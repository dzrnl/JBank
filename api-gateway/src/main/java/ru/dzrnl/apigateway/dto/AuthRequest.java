package ru.dzrnl.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRequest {
    private String login;
    private String password;
}
