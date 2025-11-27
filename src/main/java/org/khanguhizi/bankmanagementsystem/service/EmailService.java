package org.khanguhizi.bankmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cic.email.url}")
    private String emailApiUrl;

    public boolean sendOtp(String email, String otpCode) {
        try {
            // Prepare template variables
            Map<String, Object> templateVars = new HashMap<>();
            templateVars.put("otpCode", otpCode);
            templateVars.put("expiryMinutes", 5);

            // Prepare JSON request to email API
            Map<String, Object> jsonPayload = new HashMap<>();
            jsonPayload.put("email", email);
            jsonPayload.put("subject", "Your OTP Code");
            jsonPayload.put("message", "Your OTP is: " + otpCode);
            jsonPayload.put("template", "OTP");
            jsonPayload.put("templateVariables", templateVars);

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Skip-Encryption", "true");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    emailApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("OTP sent: " + response.getBody());
                return true;
            }

            System.out.println("Failed to send OTP: " + response.getBody());
            return false;

        } catch (Exception e) {
            System.err.println("Error sending OTP: " + e.getMessage());
            return false;
        }
    }
}
