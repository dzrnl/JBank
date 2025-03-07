package ru.dzrnl.bank.business.models.user;

import java.util.Objects;

public class User {
    private final String login;
    private String name;
    private int age;
    private Gender gender;
    private HairColor hairColor;

    public User(String login, String name, int age, Gender gender, HairColor hairColor) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(login, other.login);
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public HairColor getHairColor() {
        return hairColor;
    }
}
