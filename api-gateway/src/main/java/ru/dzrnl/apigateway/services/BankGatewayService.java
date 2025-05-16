package ru.dzrnl.apigateway.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dzrnl.apigateway.entities.Role;
import ru.dzrnl.apigateway.entities.User;
import ru.dzrnl.apigateway.repositories.UserRepository;

@Service
public class BankGatewayService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public BankGatewayService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createClient(Long userId, String login, String password) {
        String passwordHash = passwordEncoder.encode(password);

        User user = User.builder()
                .userId(userId)
                .login(login)
                .passwordHash(passwordHash)
                .role(Role.CLIENT)
                .build();

        userRepository.save(user);
    }

    public void createAdmin(String login, String password) {
        String passwordHash = passwordEncoder.encode(password);

        User user = User.builder()
                .login(login)
                .passwordHash(passwordHash)
                .role(Role.ADMIN)
                .build();

        userRepository.save(user);
    }
}
