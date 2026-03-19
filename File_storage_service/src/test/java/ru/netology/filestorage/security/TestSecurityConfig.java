package ru.netology.filestorage.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import ru.netology.filestorage.security.jwt.JwtTokenProvider;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        return http.build();
    }



    @Bean
    @Primary
    public JwtTokenProvider testJwtTokenProvider() {
        return new JwtTokenProvider() {
            @Override
            public String generateToken(Authentication authentication) {
                return "test-token";
            }

            @Override
            public boolean validateToken(String token) {
                return true;
            }

            @Override
            public String getUsernameFromToken(String token) {
                return "auth_test_user";
            }
        };
    }
}