package org.khanguhizi.bankmanagementsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "Customer login")
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

