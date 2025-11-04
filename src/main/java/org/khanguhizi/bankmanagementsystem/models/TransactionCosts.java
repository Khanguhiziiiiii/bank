package org.khanguhizi.bankmanagementsystem.models;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table ( name = "TransactionCosts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class TransactionCosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(updatable = false, nullable = false, length = 50)
    private double minAmount;

    @Column(updatable = false, nullable = false, length = 50)
    private double maxAmount;

    @Column(updatable = false, nullable = false, length = 50)
    private double transactionCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionTypes transactionType;
}
