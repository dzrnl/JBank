package ru.dzrnl.bank.business.contracts;

import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;
import ru.dzrnl.bank.business.models.user.User;

import java.util.Set;

public interface UserService {
    User createUser(String login, String name, int age, Gender gender, HairColor hairColor);

    User getUserByLogin(String login);

    Set<User> getAllUsers();
}
