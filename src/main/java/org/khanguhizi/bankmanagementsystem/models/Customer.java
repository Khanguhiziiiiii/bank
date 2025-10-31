package org.khanguhizi.bankmanagementsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false, nullable = false, length = 50)
    private String firstName;

    @Column(updatable = false, nullable = false, length = 50)
    private String lastName;

    @Column(updatable = false, nullable = false, unique = true, length = 50)
    private String email;

    @Column(updatable = false, nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(updatable = false, nullable = false, unique=true, length = 10)
    private String nationalId;

    @Column(updatable =false, nullable = false)
    private LocalDate dateOfBirth;

    @Column(updatable = false, nullable = false, unique = true, length = 50)
    private String username;

    @Column(updatable = false, nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR default 'USER'")
    private Role role;
}
