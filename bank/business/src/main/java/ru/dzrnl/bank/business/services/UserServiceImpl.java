package ru.dzrnl.bank.business.services;

import org.springframework.stereotype.Service;
import ru.dzrnl.bank.business.contracts.UserService;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;
import ru.dzrnl.bank.business.repositories.UserRepository;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String login, String name, int age, Gender gender, HairColor hairColor) {
        var user = User.builder()
                .login(login)
                .name(name)
                .age(age)
                .gender(gender)
                .hairColor(hairColor)
                .build();

        try {
            return userRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("User with login '" + login + "' already exists", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error occurred while saving user", e);
        }
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id '" + userId + "' not found"));
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new NoSuchElementException("User with login '" + login + "' not found"));
    }

    @Override
    public Set<User> getAllUsers() {
        return new HashSet<>(userRepository.findAll());
    }

    @Override
    public Set<User> getAllUsersFilteredByGender(Gender gender) {
        return getAllUsers().stream()
                .filter(user -> user.getGender() == gender)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getAllUsersFilteredByHairColor(HairColor hairColor) {
        return getAllUsers().stream()
                .filter(user -> user.getHairColor() == hairColor)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getAllUsersFilteredByGenderHairColor(Gender gender, HairColor hairColor) {
        return getAllUsers().stream()
                .filter(user -> user.getGender() == gender
                        && user.getHairColor() == hairColor)
                .collect(Collectors.toSet());
    }
}
