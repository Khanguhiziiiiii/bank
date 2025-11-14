package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transactions, Integer>{
    List<Transactions> findAll();
    List<Transactions> findByFromAccountOrToAccountOrderByTransactionDate(String fromAccount, String toAccount);
    List<Transactions>findByTransactionTypeIgnoreCase(String type);
    List<Transactions> findByAccount_Customer_Id(Integer id);
}
