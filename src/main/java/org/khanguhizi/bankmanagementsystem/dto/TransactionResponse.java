package org.khanguhizi.bankmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private int accountTypeId;
    private String transactionCode;
    private String transactionType;
    private double transactionCost;
    private double fromBalance;
    private double toBalance;
    private double amount;
    private String fromAccount;
    private String toAccount;
    private LocalDateTime date;
}
