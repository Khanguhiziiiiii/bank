package org.khanguhizi.bankmanagementsystem.controller;

import org.khanguhizi.bankmanagementsystem.dto.ApiResponse;
import org.khanguhizi.bankmanagementsystem.dto.LoginRequest;
import org.khanguhizi.bankmanagementsystem.dto.RegisterRequest;
import org.khanguhizi.bankmanagementsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(originPatterns = "*", origins = {"*"})
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @PostMapping ("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        var loginRes = customerService.login(loginRequest);

        return new ResponseEntity<>(loginRes, HttpStatus.OK);
    }
    @PostMapping ("/register")
    public ResponseEntity<ApiResponse> registerCustomer(@RequestBody RegisterRequest registerRequest) {
        var registerRes =  customerService.register(registerRequest);
        return new ResponseEntity<>(registerRes, HttpStatus.CREATED);


    }
}
