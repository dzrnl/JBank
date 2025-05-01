package ru.dzrnl.bank.business.models.user;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

/**
 * Represents a user with basic personal information.
 */
@Data
@Builder
public class User {

    /**
     * User id
     */
    private final Long id;

    /**
     * Unique login for the user
     */
    private final String login;

    /**
     * The name of the user
     */
    private final String name;

    /**
     * The age of the user
     */
    private final int age;

    /**
     * The gender of the user
     */
    private final Gender gender;

    /**
     * The hair color of the user
     */
    private final HairColor hairColor;

    /**
     * Checks if two users are equal based on their login.
     * Two users are considered equal if they have the same login.
     *
     * @param obj the object to compare with
     * @return {@code true} if the logins match, otherwise {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(login, other.login);
    }

    /**
     * Returns hash code based on the login.
     *
     * @return the hash code of the login
     */
    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
