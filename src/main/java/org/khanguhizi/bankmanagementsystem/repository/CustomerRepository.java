package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
 Optional<Customer> findByEmail(String email);
 Optional<Customer> findByUsername(String username);
 @Query("SELECT c FROM Customer  c WHERE c.email = ?1 OR c.username = ?1")
 Optional<Customer> findByEmailOrUsername(String usernameOrEmail);
 Optional<Customer> findByNationalId(String nationalId);
 Optional<Customer> findByPhoneNumber(String phoneNumber);
 List<Customer> findAll();
 Page<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String lastName, String email, Pageable pageable);
}