package ru.dzrnl.apigateway.data.clients;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.dzrnl.apigateway.business.clients.UserClient;
import ru.dzrnl.apigateway.business.dto.UserDto;
import ru.dzrnl.apigateway.business.dto.Gender;
import ru.dzrnl.apigateway.business.dto.HairColor;
import ru.dzrnl.apigateway.data.dto.CreateUserRequest;

import java.util.Arrays;
import java.util.List;

@Component
public class UserClientImpl extends BaseClient implements UserClient {
    public UserClientImpl(WebClient userWebClient) {
        super(userWebClient);
    }

    @Override
    public UserDto createUser(String login, String name, int age, Gender gender, HairColor hairColor) {
        var request = new CreateUserRequest(login, name, age, gender, hairColor);

        return handleResponse(
                webClient.post()
                        .uri("/api/users")
                        .bodyValue(request)
                        .retrieve(),
                UserDto.class);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return handleResponse(
                webClient.get()
                        .uri("/api/users/{id}", userId)
                        .retrieve(),
                UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        UserDto[] userDtos = handleResponse(
                webClient.get()
                        .uri("/api/users")
                        .retrieve(),
                UserDto[].class);

        return Arrays.asList(userDtos);
    }

    @Override
    public List<UserDto> getAllUsersFilteredByGender(Gender gender) {
        UserDto[] userDtos = handleResponse(
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/users")
                                .queryParam("gender", gender)
                                .build())
                        .retrieve(),
                UserDto[].class);

        return Arrays.asList(userDtos);
    }

    @Override
    public List<UserDto> getAllUsersFilteredByHairColor(HairColor hairColor) {
        UserDto[] userDtos = handleResponse(
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/users")
                                .queryParam("hairColor", hairColor)
                                .build())
                        .retrieve(),
                UserDto[].class);

        return Arrays.asList(userDtos);
    }

    @Override
    public List<UserDto> getAllUsersFilteredByGenderHairColor(Gender gender, HairColor hairColor) {
        UserDto[] userDtos = handleResponse(
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/api/users")
                                .queryParam("gender", gender)
                                .queryParam("hairColor", hairColor)
                                .build())
                        .retrieve(),
                UserDto[].class);

        return Arrays.asList(userDtos);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        handleEmptyResponse(
                webClient.post()
                        .uri("/api/users/{userId}/friends/{friendId}", userId, friendId)
                        .retrieve()
        );
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        handleEmptyResponse(
                webClient.delete()
                        .uri("/api/users/{userId}/friends/{friendId}", userId, friendId)
                        .retrieve()
        );
    }

    @Override
    public List<UserDto> getUserFriends(Long userId) {
        UserDto[] userDtos = handleResponse(
                webClient.get()
                        .uri("/api/users/{userId}/friends", userId)
                        .retrieve(),
                UserDto[].class);

        return Arrays.asList(userDtos);
    }
}
