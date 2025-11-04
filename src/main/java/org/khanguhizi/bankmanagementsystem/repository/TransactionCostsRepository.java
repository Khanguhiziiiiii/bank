package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.TransactionCosts;
import org.khanguhizi.bankmanagementsystem.models.TransactionTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionCostsRepository extends JpaRepository<TransactionCosts, Long> {
    Optional<TransactionCosts> findByTransactionTypeAndMinAmountLessThanEqualAndMaxAmountGreaterThanEqual(
            TransactionTypes transactionType, double minAmount, double maxAmount
    );
}

