package org.khanguhizi.bankmanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.models.OTP;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.khanguhizi.bankmanagementsystem.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SMSService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Value("${cic.sms.url}")
    private String smsApiUrl;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public boolean sendOtp(String phoneNumber) {
        try {
            // Fetch customer
            Customer customer = customerRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            // Generate OTP
            String otpCode = String.format("%06d", new Random().nextInt(999999));

            OTP otp = otpRepository.findByPhoneNumber(phoneNumber)
                    .orElseGet(() -> new OTP()); // create if not exists

            otp.setPhoneNumber(phoneNumber);
            otp.setOtpCode(otpCode);
            otp.setCreatedAt(LocalDateTime.now());
            otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));

            otpRepository.save(otp);

            // Build payload JSON
            Map<String, Object> payload = new HashMap<>();
            payload.put("phone", customer.getPhoneNumber());
            payload.put("template", "OTP");
            payload.put("message", "Your OTP is " + otpCode);

            Map<String, String> data = new HashMap<>();
            data.put("otp", otpCode);
            payload.put("data", data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Skip-Encryption", "true");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

            // Send request
            ResponseEntity<String> response = restTemplate.exchange(
                    smsApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("OTP sent successfully: " + response.getBody());
                return true;
            } else {
                System.err.println("Failed to send OTP: " + response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error sending OTP: " + e.getMessage());
            return false;
        }
    }
}
