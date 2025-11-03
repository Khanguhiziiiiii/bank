package org.khanguhizi.bankmanagementsystem.dto;


import lombok.Data;

@Data
public class TransactionRequest {
    private String fromAccount;
    private String toAccount;
    private double amount;
    private double fromBalance;
    private double toBalance;
    private boolean isOverdraftOptedIn;
}
