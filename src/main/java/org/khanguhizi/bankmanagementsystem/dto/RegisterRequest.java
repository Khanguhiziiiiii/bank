package org.khanguhizi.bankmanagementsystem.dto;

import lombok.Data;
import org.khanguhizi.bankmanagementsystem.models.Role;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String nationalId;
    private LocalDate dateOfBirth;
    private String username;
    private String password;
    private Role role;
}
