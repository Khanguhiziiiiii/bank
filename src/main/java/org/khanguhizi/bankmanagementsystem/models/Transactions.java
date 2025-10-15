package org.khanguhizi.bankmanagementsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table (name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Transactions {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Accounts account;

    @Column (updatable = false, nullable = false, length = 50)
    private String transactionType;

    @Column (updatable = false, unique = true, nullable = false, length = 10)
    private String transactionCode;

    @Column (updatable = false, nullable = false, length = 50)
    private LocalDateTime transactionDate;

    @Column (updatable = false, nullable = false, length = 50)
    private double balance;

    @Column (updatable = false, nullable = false, columnDefinition = "varchar not null default 'N/A'")
    private double amount;

    @Column (updatable = false, nullable = false, columnDefinition = "varchar not null default 'N/A'")
    private double fromAccount;

    @Column (updatable = false, nullable = false, columnDefinition = "varchar not null default 'N/A'")
    private double toAccount;

    @PrePersist
    public void prePersist() {
        transactionDate = LocalDateTime.now();
    }
}