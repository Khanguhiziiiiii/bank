package org.khanguhizi.bankmanagementsystem.dto;

import lombok.Data;

@Data
public class TransferFundsRequest {
    private String fromAccount;
    private String toAccount;
    private double amount;
}
