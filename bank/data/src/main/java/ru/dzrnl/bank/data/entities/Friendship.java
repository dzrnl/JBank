package ru.dzrnl.bank.data.entities;

import java.util.Objects;

/**
 * Represents a friendship between two users identified by their logins.
 */
public class Friendship {
    private final String firstUser;
    private final String secondUser;

    /**
     * Private constructor for creating a friendship between two users.
     * Ensures that the users are always ordered alphabetically to maintain consistency.
     *
     * @param firstUser  the login of the first user
     * @param secondUser the login of the second user
     */
    private Friendship(String firstUser, String secondUser) {
        if (firstUser.compareTo(secondUser) > 0) {
            String temp = firstUser;
            firstUser = secondUser;
            secondUser = temp;
        }

        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }

    /**
     * Factory method for creating a new {@link Friendship}.
     * The order of users is determined by alphabetical order of their logins.
     *
     * @param firstUser  the login of the first user
     * @param secondUser the login of the second user
     * @return a new {@link Friendship} instance
     */
    public static Friendship of(String firstUser, String secondUser) {
        return new Friendship(firstUser, secondUser);
    }

    /**
     * Gets the login of the first user in the friendship.
     *
     * @return the login of the first user
     */
    public String getFirstUser() {
        return firstUser;
    }

    /**
     * Gets the login of the second user in the friendship.
     *
     * @return the login of the second user
     */
    public String getSecondUser() {
        return secondUser;
    }

    /**
     * Checks if the given user is part of this friendship.
     *
     * @param user the login of the user to check
     * @return {@code true} if the user is part of the friendship, {@code false} otherwise
     */
    public boolean containsUser(String user) {
        return firstUser.equals(user) || secondUser.equals(user);
    }

    /**
     * Retrieves the login of the other user in the friendship.
     *
     * @param user the login of one user in the friendship
     * @return the login of the other user in the friendship
     * @throws IllegalArgumentException if the provided user is not part of this friendship
     */
    public String getFriendFor(String user) {
        if (user.equals(firstUser)) {
            return secondUser;
        } else if (user.equals(secondUser)) {
            return firstUser;
        }
        throw new IllegalArgumentException("User " + user + " is not part of this friendship");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship other = (Friendship) o;
        return Objects.equals(firstUser, other.firstUser) && Objects.equals(secondUser, other.secondUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstUser, secondUser);
    }
}
