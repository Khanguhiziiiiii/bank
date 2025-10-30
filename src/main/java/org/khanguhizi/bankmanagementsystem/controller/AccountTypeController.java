package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.AccountTypeRequest;
import org.khanguhizi.bankmanagementsystem.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(originPatterns = "*", origins = {"*"}, allowedHeaders = {"*"})
@RestController
@Tag(name = "Account Types", description = "Endpoints for managing account types")
@SecurityRequirement(name = "Bearer Token")
public class AccountTypeController {
    @Autowired
    private AccountTypeService accountTypeService;

    @Operation(
            summary = "Creates a new account type",
            description = "Creates account types from which the customers are supposed to choose from"
    )
    @PostMapping ("/createAccountType")
    public ResponseEntity <org.khanguhizi.bankmanagementsystem.dto.ApiResponse> createAccountType(@RequestBody AccountTypeRequest accountTypeRequest) {
        var createAccountTypeRes = accountTypeService.accountType(accountTypeRequest);
        return new ResponseEntity<>(createAccountTypeRes, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Fetches account types present in the database",
            description = "Restricts account creation to only the account types present in the databased"
    )
    @PostMapping("/fetchAccountType")
    public ResponseEntity <org.khanguhizi.bankmanagementsystem.dto.ApiResponse> fetchAccountType(@RequestBody AccountTypeRequest accountTypeRequest) {
        var fetchAccountTypeRes = accountTypeService.fetchAccountType(accountTypeRequest);
        return new ResponseEntity<>(fetchAccountTypeRes, HttpStatus.OK);
    }
}
