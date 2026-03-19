package ru.netology.filestorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class LoginResponse {
    @JsonProperty("auth-token")

    private String login;


    public LoginResponse(String authToken, String login, String email) {
           this.login = login;

    }

    // Геттеры и сеттеры

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

}