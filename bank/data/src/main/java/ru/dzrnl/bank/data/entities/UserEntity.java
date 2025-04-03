package ru.dzrnl.bank.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import ru.dzrnl.bank.business.models.user.Gender;
import ru.dzrnl.bank.business.models.user.HairColor;

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
}
