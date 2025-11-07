package org.khanguhizi.bankmanagementsystem.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String otpCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}

