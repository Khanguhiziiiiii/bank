package org.khanguhizi.bankmanagementsystem.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateAdminRequest {
    private String username;
    private String password;
    private String profileName;
    private String email;
    private String phoneNumber;
    private String nationalId;
}
