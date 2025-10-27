package org.khanguhizi.bankmanagementsystem.controller;

import org.khanguhizi.bankmanagementsystem.dto.AccountTypeRequest;
import org.khanguhizi.bankmanagementsystem.dto.ApiResponse;
import org.khanguhizi.bankmanagementsystem.models.AccountType;
import org.khanguhizi.bankmanagementsystem.service.AccountService;
import org.khanguhizi.bankmanagementsystem.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(originPatterns = "*", origins = {"*"}, allowedHeaders = {"*"})
@RestController
public class AccountTypeController {
    @Autowired
    private AccountTypeService accountTypeService;
    @PostMapping ("/createAccountType")
    public ResponseEntity <ApiResponse> createAccountType(@RequestBody AccountTypeRequest accountTypeRequest) {
        var createAccountTypeRes = accountTypeService.accountType(accountTypeRequest);
        return new ResponseEntity<>(createAccountTypeRes, HttpStatus.OK);
    }

    @GetMapping("/fetchAccountType")
    public ResponseEntity <ApiResponse> fetchAccountType(@RequestBody AccountTypeRequest accountTypeRequest) {
        var fetchAccountTypeRes = accountTypeService.fetchAccountType(accountTypeRequest);
        return new ResponseEntity<>(fetchAccountTypeRes, HttpStatus.OK);
    }
}
