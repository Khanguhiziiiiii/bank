package org.khanguhizi.bankmanagementsystem.dto;


import lombok.Data;

@Data
public class TransactionRequest {
    private int accountId;
    private double amount;
    private double balance;
    private boolean isOverdraftOptedIn;
}
