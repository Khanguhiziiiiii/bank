package org.khanguhizi.bankmanagementsystem.controller;

import org.khanguhizi.bankmanagementsystem.dto.ApiResponse;
import org.khanguhizi.bankmanagementsystem.dto.LoginRequest;
import org.khanguhizi.bankmanagementsystem.dto.RegisterRequest;
import org.khanguhizi.bankmanagementsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @PostMapping ("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        return customerService.login(loginRequest);
    }
    @PostMapping ("/register")
    public ApiResponse registerCustomer(@RequestBody RegisterRequest registerRequest) {
        return customerService.register(registerRequest);
    }
}
