package org.khanguhizi.bankmanagementsystem.dto;

import lombok.Data;
import org.khanguhizi.bankmanagementsystem.models.AccountType;

@Data
public class BalanceResponse {
    private String accountNumber;
    private int accountType;
    private double balance;
    private String transactionCode;
}
