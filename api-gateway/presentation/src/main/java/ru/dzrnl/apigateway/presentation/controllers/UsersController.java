package ru.dzrnl.apigateway.presentation.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.dzrnl.apigateway.business.contracts.UserService;
import ru.dzrnl.apigateway.business.dto.Gender;
import ru.dzrnl.apigateway.business.dto.HairColor;
import ru.dzrnl.apigateway.business.dto.UserDto;
import ru.dzrnl.apigateway.presentation.dto.AuthRequest;
import ru.dzrnl.apigateway.presentation.dto.CreateUserDto;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserDto createUserDto) {
        UserDto userDto = userService.createUser(
                createUserDto.getLogin(),
                createUserDto.getPassword(),
                createUserDto.getName(),
                createUserDto.getAge(),
                createUserDto.getGender(),
                createUserDto.getHairColor()
        );

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createAdmin(@RequestBody AuthRequest request) {
        userService.createAdmin(request.getLogin(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAllUsers(@RequestParam(required = false) String gender,
                                     @RequestParam(required = false) String hairColor) {
        Gender genderEnum = gender != null ? Gender.valueOf(gender.toUpperCase()) : null;
        HairColor hairColorEnum = hairColor != null ? HairColor.valueOf(hairColor.toUpperCase()) : null;

        List<UserDto> result;

        if (gender != null && hairColor != null) {
            result = userService.getAllUsersFilteredByGenderHairColor(genderEnum, hairColorEnum);
        } else if (gender != null) {
            result = userService.getAllUsersFilteredByGender(genderEnum);
        } else if (hairColor != null) {
            result = userService.getAllUsersFilteredByHairColor(hairColorEnum);
        } else {
            result = userService.getAllUsers();
        }

        return result;
    }

    @PostMapping("/{userId}/friends/{friendId}")
    @PreAuthorize("@userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<Void> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @PreAuthorize("@userSecurity.isOwner(#userId, authentication.name)")
    public ResponseEntity<String> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/friends")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId, authentication.name)")
    public List<UserDto> getUserFriends(@PathVariable Long userId) {
        return userService.getFriends(userId);
    }
}
