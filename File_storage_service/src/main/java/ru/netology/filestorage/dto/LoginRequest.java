package ru.netology.filestorage.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Login is required")
    private String login;

    @NotBlank(message = "Password is required")
    private String password;

    // Геттеры
    public String getLogin() { return login; }
    public String getPassword() { return password; }

    // Сеттеры
    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }
}
