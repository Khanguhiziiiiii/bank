package org.khanguhizi.bankmanagementsystem.dto;


import lombok.Data;

@Data
public class TransactionRequest {
    private int accountId;
    private int accountTypeId;
    private int targetAccountId;
    private double amount;
    private double balance;
}
