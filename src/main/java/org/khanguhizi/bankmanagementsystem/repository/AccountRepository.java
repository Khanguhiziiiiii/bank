package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Accounts, Integer>{
    List<Accounts> findByCustomerId(int customerId);
    Optional<Accounts> findByAccountNumber(String accountNumber);
}
