package org.khanguhizi.bankmanagementsystem.service;

import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;

import org.khanguhizi.bankmanagementsystem.utilities.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final ProfileRepository profileRepository;
    private final ProfileRoleService profileRoleService;
    private final ProfileRoleRepository profileRoleRepository;

    public ApiResponse register(RegisterRequest request){
        //Check if customer exists
        Optional<Customer> existingCustomer = customerRepository.findByEmail(request.getEmail());
        if(existingCustomer.isPresent()){
            throw new DuplicateCredentialsException("Email already exists!");
        }
        Optional<Customer> existingCustomer1 = customerRepository.findByUsername(request.getUsername());
        if(existingCustomer1.isPresent()){
            throw new DuplicateCredentialsException("Username already exists!");
        }
        var nationalIdCheck = customerRepository.findByNationalId(request.getNationalId());
        if(nationalIdCheck.isPresent()){
            throw new DuplicateCredentialsException("National Id already exists!");
        }
        var phoneNumberCheck = customerRepository.findByPhoneNumber(request.getPhoneNumber());
        if(phoneNumberCheck.isPresent()){
            throw new DuplicateCredentialsException("Phone Number already exists!");
        }

        if (!PasswordValidator.isStrongPassword(request.getPassword())) {
            throw new RuntimeException("Weak password! Must be at least 8 characters long, contain uppercase, lowercase, number, and special character.");
        }

        Profile defaultProfile = profileRepository.findByProfileName("USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found in database!"));


        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .nationalId(request.getNationalId())
                .dateOfBirth(request.getDateOfBirth())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .profile(defaultProfile)
                .blocked(false)
                .deleted(false)
                .build();
        customerRepository.save(customer);

       RegisterResponse registerResponse = new RegisterResponse();
       registerResponse.setFirstName(request.getFirstName());
       registerResponse.setLastName(request.getLastName());
       registerResponse.setEmail(request.getEmail());
       registerResponse.setPhoneNumber(request.getPhoneNumber());
       registerResponse.setNationalId(request.getNationalId());
       registerResponse.setDateOfBirth(request.getDateOfBirth());
       registerResponse.setUsername(request.getUsername());
       registerResponse.setRole(customer.getProfile().getProfileName());
       return ApiResponse.builder()
               .message("Successfully registered!")
               .data(registerResponse)
               .status("success")
               .build();
    }

    public ApiResponse login(LoginRequest request) {
        Optional<Customer> existingCustomer = customerRepository.findByEmailOrUsername(request.getUsernameOrEmail());
        if (existingCustomer.isEmpty()) {
            throw new InvalidCredentialsException("Invalid email or password!");
        }


        Customer customer = existingCustomer.get();

        Customer customerId = customer;

        Profile profile = customer.getProfile();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setFirstName(customer.getFirstName());
        loginResponse.setLastName(customer.getLastName());
        loginResponse.setEmail(customer.getEmail());
        loginResponse.setPhoneNumber(customer.getPhoneNumber());
        loginResponse.setNationalId(customer.getNationalId());
        loginResponse.setDateOfBirth(customer.getDateOfBirth());
        loginResponse.setUsername(customer.getUsername());
        loginResponse.setProfile(customer.getProfile().getProfileName());
        loginResponse.setCustomerId(customerId.getId());

        loginResponse.setToken(
                jwtService.generateToken(
                        new org.springframework.security.core.userdetails.User(
                                customer.getUsername(),
                                customer.getPassword(),
                                profileRoleRepository.findByProfile_ProfileName(profile.getProfileName())
                                        .stream()
                                        .map(pr -> "ROLE_" + pr.getRole().getRole().toUpperCase())
                                        .map(SimpleGrantedAuthority::new)
                                        .toList()
                        )
                ));

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password!");
        } else if (customer.getBlocked()==true) {
            throw new InvalidAccessException("Blocked user!");
        } else if (customer.getDeleted()==true) {
            throw new InvalidAccessException("Deleted account!");
        }
        else{
            return ApiResponse.builder()
                    .message("Login Successful!")
                    .data(loginResponse)
                    .status(String.valueOf(HttpStatus.OK))
                    .build();
        }
    }
}