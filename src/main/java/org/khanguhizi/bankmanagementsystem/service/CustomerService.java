package org.khanguhizi.bankmanagementsystem.service;

import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.Customer;
import org.khanguhizi.bankmanagementsystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService {
    private final CustomerRepository customerRepository;


    public ApiResponse register(RegisterRequest request){
        //Check if customer exists
        Optional<Customer> existingCustomer = customerRepository.findByEmail(request.getEmail());
        if(existingCustomer.isPresent()){
            throw new IllegalArgumentException("Email already exists!");
        }
        Optional<Customer> existingCustomer1 = customerRepository.findByUsername(request.getUsername());
        if(existingCustomer1.isPresent()){
            throw new IllegalArgumentException("Username already exists!");
        }
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .nationalId(request.getNationalId())
                .dateOfBirth(request.getDateOfBirth())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
        customerRepository.save(customer);

       RegisterResponse registerResponse = new RegisterResponse();
       registerResponse.setEmail(request.getEmail());
       registerResponse.setUsername(request.getUsername());
       return ApiResponse.builder()
               .message("Successfully registered!")
               .data(registerResponse)
               .status("success")
               .build();
    }

    public ApiResponse login(LoginRequest request){
        Optional<Customer> existingCustomer = customerRepository.findByEmailOrUsername(request.getUsernameOrEmail());
        if(existingCustomer.isEmpty()){
            throw new IllegalArgumentException("Invalid email or password!");
        }
        Customer customer = existingCustomer.get();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setEmail(request.getEmail());
        loginResponse.setUsername(request.getUsername());

        if(request.getPassword().equals(customer.getPassword())){
            throw new IllegalArgumentException("Invalid email or password!");
        }else{
            return ApiResponse.builder()
                    .message("Login Successful!")
                    .data(loginResponse)
                    .status("success")
                    .build();
        }

    }
}
