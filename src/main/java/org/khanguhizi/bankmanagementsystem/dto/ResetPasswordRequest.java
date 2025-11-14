package org.khanguhizi.bankmanagementsystem.dto;

import lombok.*;

@Data
public class ResetPasswordRequest {
    private String otp;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String phoneNumber;
}
