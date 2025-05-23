package ru.dzrnl.apigateway.business.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dzrnl.apigateway.business.clients.UserClient;
import ru.dzrnl.apigateway.business.contracts.UserService;
import ru.dzrnl.apigateway.business.dto.FriendSummaryDto;
import ru.dzrnl.apigateway.business.dto.Gender;
import ru.dzrnl.apigateway.business.dto.HairColor;
import ru.dzrnl.apigateway.business.dto.UserDto;
import ru.dzrnl.apigateway.business.models.Role;
import ru.dzrnl.apigateway.business.models.User;
import ru.dzrnl.apigateway.business.repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserClient userClient, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userClient = userClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(String login, String password, String name, int age, Gender gender, HairColor hairColor) {
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .login(login)
                .passwordHash(encodedPassword)
                .role(Role.CLIENT)
                .build();

        userRepository.save(user);

        return userClient.createUser(login, name, age, gender, hairColor);
    }

    @Override
    public void createAdmin(String login, String password) {
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .login(login)
                .passwordHash(encodedPassword)
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);
    }

    @Override
    public UserDto getUserById(long userId) {
        return userClient.getUserById(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userClient.getAllUsers();
    }

    @Override
    public List<UserDto> getAllUsersFilteredByGender(Gender gender) {
        return userClient.getAllUsersFilteredByGender(gender);
    }

    @Override
    public List<UserDto> getAllUsersFilteredByHairColor(HairColor hairColor) {
        return userClient.getAllUsersFilteredByHairColor(hairColor);
    }

    @Override
    public List<UserDto> getAllUsersFilteredByGenderHairColor(Gender gender, HairColor hairColor) {
        return userClient.getAllUsersFilteredByGenderHairColor(gender, hairColor);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        userClient.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        userClient.removeFriend(userId, friendId);
    }

    @Override
    public List<UserDto> getFriends(long userId) {
        return userClient.getUserFriends(userId);
    }

    @Override
    public List<FriendSummaryDto> getFriendSummaries(long userId) {
        List<UserDto> friends = userClient.getUserFriends(userId);
        return friends.stream()
                .map(friend -> new FriendSummaryDto(friend.getId(), friend.getName()))
                .toList();
    }
}
