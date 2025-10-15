package org.khanguhizi.bankmanagementsystem.controller;

import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.Accounts;
import org.khanguhizi.bankmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(originPatterns = "*", origins = {"*"})
@RestController
public class AccountController {
@Autowired
    private AccountService accountService;
    @PostMapping ("/createAccount")
    public ResponseEntity<ApiResponse> createAccount(@RequestBody AccountRequest accountRequest){
        var createAccountRes = accountService.account(accountRequest);
        return new ResponseEntity<>(createAccountRes, HttpStatus.OK);
    }

    @PostMapping ("/fetchAccounts")
    public ResponseEntity<ApiResponse> fetchAccounts(@RequestBody AccountRequest accountRequest){
        var fetchAccountsRes = accountService.fetchAccountsByCustomer(accountRequest);
        return new ResponseEntity<>(fetchAccountsRes, HttpStatus.OK);
    }
}
