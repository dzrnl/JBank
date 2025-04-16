package ru.dzrnl.bank.business.test;

import org.junit.jupiter.api.Test;
import ru.dzrnl.bank.business.contracts.UserService;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.business.repositories.UserRepository;
import ru.dzrnl.bank.business.services.UserServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private final User defaultUser = getDefaultUser();
    private final User secondDefaultUser = getSecondDefaultUser();

    @Test
    public void shouldCreateUser() {
        String userLogin = defaultUser.getLogin();
        String userName = defaultUser.getName();
        int age = defaultUser.getAge();
        Gender gender = defaultUser.getGender();
        HairColor hairColor = defaultUser.getHairColor();

        UserRepository mockRepo = mock(UserRepository.class);
        doNothing().when(mockRepo).saveUser(argThat(user -> user.getLogin().equals(userLogin)));

        UserService userService = new UserServiceImpl(mockRepo);

        userService.createUser(userLogin, userName, age, gender, hairColor);

        verify(mockRepo, times(1)).saveUser(argThat(user -> user.getLogin().equals(userLogin)
                && user.getName().equals(userName)
                && user.getAge() == age
                && user.getGender() == gender
                && user.getHairColor() == hairColor));
    }

    @Test
    public void shouldThrowExceptionIfUserAlreadyExists() {
        String userLogin = defaultUser.getLogin();
        String userName = defaultUser.getName();
        int age = defaultUser.getAge();
        Gender gender = defaultUser.getGender();
        HairColor hairColor = defaultUser.getHairColor();

        UserRepository mockRepo = mock(UserRepository.class);
        doThrow(new IllegalArgumentException())
                .when(mockRepo).saveUser(argThat(user -> user.getLogin().equals(userLogin)));

        UserService userService = new UserServiceImpl(mockRepo);

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(userLogin, userName, age, gender, hairColor));
    }

    @Test
    public void shouldGetUserByLogin() {
        String userLogin = defaultUser.getLogin();

        UserRepository mockRepo = mock(UserRepository.class);
        when(mockRepo.findUserByLogin(userLogin)).thenReturn(Optional.of(defaultUser));

        UserService userService = new UserServiceImpl(mockRepo);

        User foundUser = userService.getUserByLogin(userLogin);

        assertNotNull(foundUser);
        assertEquals(userLogin, foundUser.getLogin());
    }

    @Test
    public void shouldNotGetUserByLogin() {
        String userLogin = defaultUser.getLogin();

        UserRepository mockRepo = mock(UserRepository.class);
        when(mockRepo.findUserByLogin(userLogin)).thenReturn(Optional.empty());

        UserService userService = new UserServiceImpl(mockRepo);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> userService.getUserByLogin(userLogin));

        assertEquals("User with login '" + userLogin + "' not found", exception.getMessage());
    }

    @Test
    public void shouldGetAllUsers() {
        var users = List.of(defaultUser, secondDefaultUser);

        UserRepository mockRepo = mock(UserRepository.class);

        UserService userService = new UserServiceImpl(mockRepo);

        assertTrue(userService.getAllUsers().isEmpty());

        when(mockRepo.findAllUsers()).thenReturn(users);

        assertEquals(userService.getAllUsers(), new HashSet<>(users));
    }

    private static User getDefaultUser() {
        String userLogin = "ivanov";
        String userName = "Ivan Ivanov";
        int age = 30;
        Gender gender = Gender.MALE;
        HairColor hairColor = HairColor.BROWN;

        return new User(userLogin, userName, age, gender, hairColor);
    }

    private static User getSecondDefaultUser() {
        String userLogin = "petrov";
        String userName = "Petar Petrov";
        int age = 20;
        Gender gender = Gender.MALE;
        HairColor hairColor = HairColor.BLACK;

        return new User(userLogin, userName, age, gender, hairColor);
    }
}
