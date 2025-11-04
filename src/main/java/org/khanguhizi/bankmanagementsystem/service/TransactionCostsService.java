package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.models.TransactionTypes;
import org.khanguhizi.bankmanagementsystem.repository.TransactionCostsRepository;
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
}

