package ru.dzrnl.bank.presentation.controllers;

import org.springframework.web.bind.annotation.*;
import ru.dzrnl.bank.business.contracts.UserService;
import ru.dzrnl.bank.presentation.dto.UserDto;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        return UserDto.fromDomain(userService.getUserById(userId));
    }
}