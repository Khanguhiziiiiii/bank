package org.khanguhizi.bankmanagementsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table (name = "transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Transactions {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Accounts account;

    @Column (updatable = false, nullable = false, length = 50)
    private String transactionType;

    @Column (updatable = false, nullable = false, length = 50)
    private LocalDateTime transactionDate;

    @PrePersist
    public void prePersist() {
        transactionDate = LocalDateTime.now();
    }
}