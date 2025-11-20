package org.khanguhizi.bankmanagementsystem.service;

import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.models.OTP;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import org.khanguhizi.bankmanagementsystem.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OTPRepository otpRepository;

    @Value("${cic.email.url}")
    private String emailApiUrl;

    public boolean sendOtp(String email){
        try{
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Customer with email " + email + " not found"));

            String otpCode = String.format("%06d", new Random().nextInt(999999));

            OTP otp = otpRepository.findByEmail(email)
                    .orElseGet(() -> new OTP());

            otp.setPhoneNumber(customer.getPhoneNumber());
            otp.setEmail(email);
            otp.setOtpCode(otpCode);
            otp.setCreatedAt(LocalDateTime.now());
            otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));

            otpRepository.save(otp);

            MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
            formData.add("email", customer.getEmail());
            formData.add("subject", "You are welcome to CIC");
            formData.add("message", "Your OTP is " + otpCode);
            formData.add("template", "OTP");

            Map<String, Object> templateVars = new HashMap<>();
            templateVars.put("otpCode", otpCode);
            templateVars.put("expiryMinutes", 5);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("multipart/form-data"));
            headers.set("Skip-Encryption", "true");

            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(formData, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    emailApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()){
                System.out.println("OTP sent successfully: " + response.getBody());
                return true;
            } else {
                System.out.println("Failed to send OTP: " + response.getBody());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error sending OTP: " + e.getMessage());
            return false;
        }
    }
}
