package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.AccountRequest;
import org.khanguhizi.bankmanagementsystem.models.Accounts;
import org.khanguhizi.bankmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(originPatterns = "*", origins = {"*"}, allowedHeaders = {"*"})

@RestController
@Tag(name = "Accounts", description = "Endpoints for managing bank accounts")
@SecurityRequirement(name = "Bearer Token")
public class AccountController {
@Autowired
    private AccountService accountService;

    @Operation(
            summary = "Create a new bank account",
            description = "Creates a new bank account for an authenticated customer."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid account creation request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token")
    })
    @PostMapping ("/createAccount")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> createAccount(@RequestBody AccountRequest accountRequest){
        var createAccountRes = accountService.account(accountRequest);
        return new ResponseEntity<>(createAccountRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Fetch customer accounts",
            description = "Fetches all accounts associated with a given customer ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts successfully retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping ("/fetchAccounts")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> fetchAccounts(@RequestBody AccountRequest accountRequest){
        var fetchAccountsRes = accountService.fetchAccountsByCustomer(accountRequest);
        return new ResponseEntity<>(fetchAccountsRes, HttpStatus.OK);
    }
}
