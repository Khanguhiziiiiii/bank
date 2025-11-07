package org.khanguhizi.bankmanagementsystem.service;

import org.khanguhizi.bankmanagementsystem.dto.ApiResponse;
import org.khanguhizi.bankmanagementsystem.dto.ForgotPasswordRequest;
import org.khanguhizi.bankmanagementsystem.repository.OTPRepository;
import org.khanguhizi.bankmanagementsystem.models.OTP;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ForgotPasswordService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private SMSService smsService;

    public ApiResponse forgotPassword(ForgotPasswordRequest request) {
        var customer = customerRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        String otp = String.format("%06d", new Random().nextInt(999999));

        otpRepository.deleteByPhoneNumber(request.getPhoneNumber());

        OTP otpEntity = new OTP();
        otpEntity.setPhoneNumber(request.getPhoneNumber());
        otpEntity.setOtpCode(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otpEntity);

        smsService.sendOtp(request.getPhoneNumber());

        return ApiResponse.builder()
                .status(String.valueOf(true))
                .message("OTP sent successfully to " + request.getPhoneNumber())
                .build();
    }
}
