package ru.dzrnl.bank.data.entities;

import java.util.Objects;

public class Friendship {
    private final String user1;
    private final String user2;

    private Friendship(String user1, String user2) {
        if (user1.compareTo(user2) > 0) {
            String temp = user1;
            user1 = user2;
            user2 = temp;
        }

        this.user1 = user1;
        this.user2 = user2;
    }

    public static Friendship of(String user1, String user2) {
        return new Friendship(user1, user2);
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public boolean containsUser(String user) {
        return user1.equals(user) || user2.equals(user);
    }

    public String getFriendFor(String user) {
        if (user.equals(user1)) {
            return user2;
        } else if (user.equals(user2)) {
            return user1;
        }
        throw new IllegalArgumentException("User " + user + " is not part of this friendship");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship other = (Friendship) o;
        return Objects.equals(user1, other.user1) && Objects.equals(user2, other.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }
}
