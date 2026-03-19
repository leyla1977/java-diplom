package ru.netology.filestorage.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.netology.filestorage.entity.User;
import ru.netology.filestorage.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner init(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByLogin("user").isEmpty()) {
                User user = new User();
                user.setLogin("user");
                user.setEmail("user@example.com");  // Обязательно!
                user.setPassword(passwordEncoder.encode("password"));
                user.setEnabled(true);  // Можно добавить, если нужно
                userRepository.save(user);

                System.out.println("Создан тестовый пользователь: user / password");
            }
        };
    }
}