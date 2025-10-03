package org.khanguhizi.bankmanagementsystem.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String username;
    private String password;
    private String usernameOrEmail;
}

