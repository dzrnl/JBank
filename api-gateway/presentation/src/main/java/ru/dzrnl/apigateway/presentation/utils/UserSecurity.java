package ru.dzrnl.apigateway.presentation.utils;

import org.springframework.stereotype.Component;
import ru.dzrnl.apigateway.business.contracts.UserService;
import ru.dzrnl.apigateway.business.dto.UserDto;

@Component
public class UserSecurity {
    private final UserService userService;

    public UserSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean isOwner(Long userId, String currentLogin) {
        if (currentLogin == null) {
            return false;
        }

        UserDto user = userService.getUserById(userId);

        if (user == null) {
            return false;
        }

        return currentLogin.equals(user.getLogin());
    }
}
