package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.Accounts;
import org.khanguhizi.bankmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(originPatterns = "*", origins = {"*"}, allowedHeaders = {"*"})

@RestController
@Tag(name = "Accounts", description = "Endpoints for managing bank accounts")
@SecurityRequirement(name = "Bearer Token")
public class AccountController {
@Autowired
    private AccountService accountService;

@Autowired
    private AdminDashboardService adminDashboardService;

    @Operation(
            summary = "Create a new bank account",
            description = "Creates a new bank account for an authenticated customer."
    )
    @PostMapping ("/createAccount")
    public ResponseEntity<org.khanguhizi.bankmanagementsystem.dto.ApiResponse> createAccount(@RequestBody AccountRequest accountRequest){
        var createAccountRes = accountService.account(accountRequest);
        return new ResponseEntity<>(createAccountRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Fetch customer accounts",
            description = "Fetches all accounts associated with a given customer ID."
    )
    @GetMapping("/customer/{customerId}/accounts")
    public ResponseEntity<ApiResponse> fetchAccounts(@PathVariable int customerId) {

        AccountRequest request = new AccountRequest();
        request.setCustomerId(customerId);

        var response = accountService.fetchAccountsByCustomer(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "fetches details about an account"
    )
    @GetMapping("/get/account{accountId}/details")
    public ResponseEntity<ApiResponse> getAccountDetails(
            @PathVariable Integer accountId
    ) {
        ApiResponse response = accountService.getAccountDetails(accountId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "updates the status of an account (active or inactive)"
    )
    @PatchMapping("/admin/update/account{accountId}/status")
    public ResponseEntity<ApiResponse> updateAccountStatus(
            @PathVariable Integer accountId,
            @RequestParam boolean active
    ) {
        ApiResponse response = adminDashboardService.updateAccountStatus(accountId, active);
        return ResponseEntity.ok(response);
    }

}
