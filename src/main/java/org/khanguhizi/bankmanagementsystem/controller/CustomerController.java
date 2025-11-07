package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.service.CustomerService;
import org.khanguhizi.bankmanagementsystem.service.ForgotPasswordService;
import org.khanguhizi.bankmanagementsystem.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//@CrossOrigin(originPatterns = "*", origins = {"*"}, allowedHeaders = {"*"})

@Tag(name = "Customer Authentication", description = "Endpoints for customer login and registration")
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @Autowired
    private ResetPasswordService resetPasswordService;

    @Operation(
            summary = "Authenticate a customer",
            description = "Validates customer credentials and returns a JWT token upon successful login."
    )
    @PostMapping ("/login")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        var loginRes = customerService.login(loginRequest);
        return new ResponseEntity<>(loginRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Register a new customer",
            description = "Creates a new customer account using the provided registration details."
    )
    @PostMapping ("/register")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> registerCustomer(@RequestBody RegisterRequest registerRequest) {
        var registerRes =  customerService.register(registerRequest);
        return new ResponseEntity<>(registerRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Forgot Password - Send OTP via SMS"
    )
    @PostMapping("/forgotPassword")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> forgotPassword(@RequestBody  ForgotPasswordRequest request) {
        var response = forgotPasswordService.forgotPassword(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "Reset Password - Verify OTP and update password"
    )
    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        var response = resetPasswordService.resetPassword(resetPasswordRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
