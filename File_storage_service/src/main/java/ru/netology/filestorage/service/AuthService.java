package ru.netology.filestorage.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.netology.filestorage.dto.LoginRequest;
import ru.netology.filestorage.dto.LoginResponse;
import ru.netology.filestorage.entity.User;
import ru.netology.filestorage.repository.UserRepository;
import ru.netology.filestorage.security.jwt.JwtTokenProvider;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;  // Добавь это поле

    // Обновляем конструктор
    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;  // Добавь эту строку
    }

    public LoginResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        String jwt = jwtTokenProvider.generateToken(authentication);

        // Получи пользователя из базы
        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new LoginResponse(jwt, user.getLogin(), user.getEmail());
    }
}