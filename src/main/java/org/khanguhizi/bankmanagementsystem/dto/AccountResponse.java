package org.khanguhizi.bankmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountResponse {
    private String accountNumber;
    private String accountType;
    private double balance;
}
