package org.khanguhizi.bankmanagementsystem.dto;

import lombok.Data;

@Data
public class TransferFundsResponse {
    private String fromAccount;
    private double fromBalance;
    private double amount;
    private String toAccount;
    private double toBalance;
    private String transactionCode;
    private double transactionCost;
}
