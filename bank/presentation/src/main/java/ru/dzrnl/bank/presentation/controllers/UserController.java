package ru.dzrnl.bank.presentation.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dzrnl.bank.business.contracts.FriendshipService;
import ru.dzrnl.bank.business.contracts.UserService;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.presentation.dto.UserDto;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.createUser(userDto.getLogin(),
                    userDto.getName(),
                    userDto.getAge(),
                    userDto.getGender(),
                    userDto.getHairColor());

            return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.fromDomain(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable long userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(UserDto.fromDomain(user));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add a friend for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend added successfully"),
            @ApiResponse(responseCode = "404", description = "User or friend not found")
    })
    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable long userId, @PathVariable long friendId) {
        try {
            User user = userService.getUserById(userId);
            User friend = userService.getUserById(friendId);

            friendshipService.addFriend(user.getLogin(), friend.getLogin());
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remove a friend from the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed successfully"),
            @ApiResponse(responseCode = "404", description = "User or friend not found")
    })
    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        try {
            User user = userService.getUserById(userId);
            User friend = userService.getUserById(friendId);

            friendshipService.removeFriend(user.getLogin(), friend.getLogin());
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get a user's friends by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of friends retrieved successfully")
    })
    @GetMapping("/{userId}/friends")
    public List<UserDto> getUserFriends(@PathVariable long userId) {
        return friendshipService.getFriends(userId).stream()
                .map(UserDto::fromDomain)
                .toList();
    }

    @Operation(summary = "Get all users with optional filtering by gender and hair color")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid gender or hair color filter value")
    })
    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false) String gender,
                                     @RequestParam(required = false) String hairColor) {
        Gender genderEnum = gender != null ? Gender.valueOf(gender.toUpperCase()) : null;
        HairColor hairColorEnum = hairColor != null ? HairColor.valueOf(hairColor.toUpperCase()) : null;

        return userService.getAllUsers().stream()
                .filter(user -> (genderEnum == null || user.getGender() == genderEnum)
                        && (hairColorEnum == null || user.getHairColor() == hairColorEnum))
                .map(UserDto::fromDomain)
                .toList();
    }
}