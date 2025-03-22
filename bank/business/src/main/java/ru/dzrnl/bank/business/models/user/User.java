package ru.dzrnl.bank.business.models.user;

import java.util.Objects;

/**
 * Represents a user with basic personal information.
 *
 * @param login     unique login for the user
 * @param name      the name of the user
 * @param age       the age of the user
 * @param gender    the gender of the user
 * @param hairColor the hair color of the user
 */
public record User(String login, String name, int age, Gender gender, HairColor hairColor) {

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
