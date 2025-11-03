package org.khanguhizi.bankmanagementsystem.dto;

import lombok.Data;
import org.khanguhizi.bankmanagementsystem.models.AccountType;

@Data
public class BalanceResponse {
    private String fromAccount;
    private int accountType;
    private double fromBalance;
    private String transactionCode;
}
