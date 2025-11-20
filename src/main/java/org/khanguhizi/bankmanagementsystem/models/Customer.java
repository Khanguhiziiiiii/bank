package org.khanguhizi.bankmanagementsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

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

    @Column(updatable = false, nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(updatable = false, nullable = false, unique=true, length = 10)
    private String nationalId;

    @Column(updatable =false, nullable = false)
    private LocalDate dateOfBirth;

    @Column(updatable = false, nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne
    @JoinColumn(name = "profile", referencedColumnName = "profileName")
    private Profile profile;

    @Column(updatable = true, nullable = false, columnDefinition = "boolean default false")
    private Boolean deleted;

    @Column(updatable = true, nullable = false, columnDefinition = "boolean default false")
    private Boolean blocked;
}
