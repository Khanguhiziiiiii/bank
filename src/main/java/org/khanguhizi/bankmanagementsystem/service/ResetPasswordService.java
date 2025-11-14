package org.khanguhizi.bankmanagementsystem.service;

import org.khanguhizi.bankmanagementsystem.dto.ApiResponse;
import org.khanguhizi.bankmanagementsystem.dto.ResetPasswordRequest;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.models.OTP;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.khanguhizi.bankmanagementsystem.repository.OTPRepository;
import org.khanguhizi.bankmanagementsystem.utilities.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ResetPasswordService {

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ApiResponse resetPassword(ResetPasswordRequest request) {
        OTP otpRecord = otpRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("No OTP found for this number"));

        if (otpRecord.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!otpRecord.getOtpCode().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        Customer customer = customerRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
            throw new RuntimeException("Current password is incorrect!");
        }

        if  (passwordEncoder.matches(request.getNewPassword(), customer.getPassword())) {
            throw new RuntimeException("New password cannot match old password!");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New passwords do not match!");
        }

        if (!PasswordValidator.isStrongPassword(request.getNewPassword())) {
            throw new RuntimeException("Weak password! Must be at least 8 characters long, contain uppercase, lowercase, number, and special character.");
        }


        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.save(customer);

        otpRepository.deleteByPhoneNumber(request.getPhoneNumber());

        return ApiResponse.builder()
                .status(String.valueOf(true))
                .message("Password reset successfully.")
                .build();
    }
}
