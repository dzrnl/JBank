package ru.dzrnl.apigateway.business.contracts;

import ru.dzrnl.apigateway.business.dto.FriendSummaryDto;
import ru.dzrnl.apigateway.business.dto.Gender;
import ru.dzrnl.apigateway.business.dto.HairColor;
import ru.dzrnl.apigateway.business.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(String login, String password, String name, int age, Gender gender, HairColor hairColor);

    void createAdmin(String login, String password);

    UserDto getUserById(long userId);

    List<UserDto> getAllUsers();

    List<UserDto> getAllUsersFilteredByGender(Gender gender);

    List<UserDto> getAllUsersFilteredByHairColor(HairColor hairColor);

    List<UserDto> getAllUsersFilteredByGenderHairColor(Gender gender, HairColor hairColor);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<UserDto> getFriends(long userId);

    List<FriendSummaryDto> getFriendSummaries(long userId);
}
