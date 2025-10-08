package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
public interface AccountRepository extends JpaRepository<Accounts, Integer>{
    List<Accounts> findByCustomerId(int customerId);
}
