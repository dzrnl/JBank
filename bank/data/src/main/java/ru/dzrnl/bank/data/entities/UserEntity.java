package ru.dzrnl.bank.data.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;

    private String name;

    private Integer age;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Gender gender;

    @Enumerated
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private HairColor hairColor;

    @ManyToMany
    @JoinTable(
            name = "friendships",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"})
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserEntity> friends;
}
