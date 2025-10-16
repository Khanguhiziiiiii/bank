package org.khanguhizi.bankmanagementsystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoginRequest {
    private String email;
    private String username;
    private String password;
    private String usernameOrEmail;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String nationalId;
    private LocalDate dateOfBirth;
}

