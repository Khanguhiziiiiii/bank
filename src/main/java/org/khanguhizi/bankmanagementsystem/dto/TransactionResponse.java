package org.khanguhizi.bankmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private int accountTypeId;
    private String transactionCode;
    private double fromBalance;
    private double toBalance;
    private double amount;
    private String fromAccount;
    private String toAccount;
}
