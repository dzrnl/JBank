package ru.dzrnl.apigateway.business.clients;

import ru.dzrnl.apigateway.business.dto.UserDto;
import ru.dzrnl.apigateway.business.dto.Gender;
import ru.dzrnl.apigateway.business.dto.HairColor;

import java.util.List;

public interface UserClient {
    UserDto createUser(String login, String name, int age, Gender gender, HairColor hairColor);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    List<UserDto> getAllUsersFilteredByGender(Gender gender);

    List<UserDto> getAllUsersFilteredByHairColor(HairColor hairColor);

    List<UserDto> getAllUsersFilteredByGenderHairColor(Gender gender, HairColor hairColor);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<UserDto> getUserFriends(Long userId);
}
