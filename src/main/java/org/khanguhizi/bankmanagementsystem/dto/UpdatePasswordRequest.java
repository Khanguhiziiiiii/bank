package org.khanguhizi.bankmanagementsystem.dto;

import lombok.*;

@Data
public class UpdatePasswordRequest {
    private String usernameOrEmail;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
