package org.khanguhizi.bankmanagementsystem.service;

import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.service.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UpdatePasswordService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ApiResponse updatePassword(UpdatePasswordRequest request) {
        Optional<Customer> existingCustomer = customerRepository.findByEmailOrUsername(request.getUsernameOrEmail());
        if (existingCustomer.isEmpty()) {
            throw new NoAccountsFoundException("No user found with username " + request.getUsernameOrEmail());
        }

        Customer customer = existingCustomer.get();

        if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
            throw new RuntimeException("Current password is incorrect!");
        }

        if  (passwordEncoder.matches(request.getNewPassword(), customer.getPassword())) {
            throw new RuntimeException("New password cannot match old password!");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New passwords do not match!");
        }

        customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        customerRepository.saveAndFlush(customer);

        return ApiResponse.builder()
                .status(String.valueOf(HttpStatus.OK.value()))
                .message("Password changed successfully.")
                .build();
    }
}
