package ru.dzrnl.apigateway.presentation.utils;

import org.springframework.stereotype.Component;
import ru.dzrnl.apigateway.business.contracts.AccountService;
import ru.dzrnl.apigateway.business.contracts.UserService;
import ru.dzrnl.apigateway.business.dto.accounts.AccountDto;
import ru.dzrnl.apigateway.business.dto.users.UserDto;

@Component
public class UserSecurity {
    private final UserService userService;
    private final AccountService accountService;

    public UserSecurity(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
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

    public boolean isAccountOwner(Long accountId, String currentLogin) {
        if (currentLogin == null) {
            return false;
        }

        AccountDto account = accountService.getAccount(accountId);

        if (account == null) {
            return false;
        }

        return currentLogin.equals(account.getOwnerLogin());
    }
}
