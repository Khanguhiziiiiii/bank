package org.khanguhizi.bankmanagementsystem.dto;

import lombok.*;
import org.khanguhizi.bankmanagementsystem.models.TransactionTypes;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPreviewResponse {
    private TransactionTypes transactionType;
    private double transactionCost;
    private double amount;
    private String toAccount;
    private String fromAccount;
}
