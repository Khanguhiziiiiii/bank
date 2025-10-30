package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.AccountType;
import org.khanguhizi.bankmanagementsystem.repository.AccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer>{
    Optional<AccountType> findByAccountType(String accountType);
    Optional<AccountType> findById(Integer id);
    List<AccountType> findAll();
}
