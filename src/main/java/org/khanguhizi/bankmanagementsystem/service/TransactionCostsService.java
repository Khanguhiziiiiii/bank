package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.TransactionCosts;
import org.khanguhizi.bankmanagementsystem.models.TransactionTypes;
import org.khanguhizi.bankmanagementsystem.repository.TransactionCostsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionCostsService {

    private final TransactionCostsRepository transactionCostsRepository;

    public double getTransactionCost(double amount, TransactionTypes transactionType) {
        return transactionCostsRepository
                .findByTransactionTypeAndMinAmountLessThanEqualAndMaxAmountGreaterThanEqual(
                        transactionType, amount, amount
                )
                .map(cost -> cost.getTransactionCost())
                .orElse(0.0);
    }

    public ApiResponse addCost(TransactionCostsRequest request) {
        TransactionCosts transactionCosts = TransactionCosts.builder()
                .minAmount(request.getMinimumAmount())
                .maxAmount(request.getMaximumAmount())
                .transactionCost(request.getTransactionCost())
                .transactionType(request.getTransactionType())
                .build();
        transactionCostsRepository.save(transactionCosts);
        TransactionCostsResponse transactionCostsResponse = new TransactionCostsResponse();
        transactionCostsResponse.setMinimumAmount(transactionCosts.getMinAmount());
        transactionCostsResponse.setMaximumAmount(transactionCosts.getMaxAmount());
        transactionCostsResponse.setTransactionCost(transactionCosts.getTransactionCost());
        transactionCostsResponse.setTransactionType(transactionCosts.getTransactionType());

        return ApiResponse.builder()
                .message("Transaction Cost Updated successfully!")
                .data(transactionCostsResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}

