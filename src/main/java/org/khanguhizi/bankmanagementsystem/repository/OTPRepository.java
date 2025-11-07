package org.khanguhizi.bankmanagementsystem.repository;

import org.khanguhizi.bankmanagementsystem.models.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByPhoneNumber(String phoneNumber);
    @Modifying
    @Transactional
    @Query("DELETE FROM OTP o WHERE o.phoneNumber = :phoneNumber")
    void deleteByPhoneNumber(String phoneNumber);
}
