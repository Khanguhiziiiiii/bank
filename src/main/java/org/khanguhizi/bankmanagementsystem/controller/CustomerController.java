package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.LoginRequest;
import org.khanguhizi.bankmanagementsystem.dto.RegisterRequest;
import org.khanguhizi.bankmanagementsystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


//@CrossOrigin(originPatterns = "*", origins = {"*"}, allowedHeaders = {"*"})

@Tag(name = "Customer Authentication", description = "Endpoints for customer login and registration")
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(
            summary = "Authenticate a customer",
            description = "Validates customer credentials and returns a JWT token upon successful login."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "400", description = "Invalid request format")
    })
    @PostMapping ("/login")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        var loginRes = customerService.login(loginRequest);
        return new ResponseEntity<>(loginRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Register a new customer",
            description = "Creates a new customer account using the provided registration details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid or incomplete registration data"),
            @ApiResponse(responseCode = "409", description = "Email or username already exists")
    })
    @PostMapping ("/register")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> registerCustomer(@RequestBody RegisterRequest registerRequest) {
        var registerRes =  customerService.register(registerRequest);
        return new ResponseEntity<>(registerRes, HttpStatus.CREATED);


    }
}
