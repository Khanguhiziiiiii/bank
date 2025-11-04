package org.khanguhizi.bankmanagementsystem.dto;

import lombok.*;
import org.khanguhizi.bankmanagementsystem.models.TransactionTypes;

@Data
public class TransactionCostsResponse {
    private double minimumAmount;
    private double maximumAmount;
    private double transactionCost;
    private TransactionTypes transactionType;
}
