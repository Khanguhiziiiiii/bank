package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.*;
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
@Tag(name = "Transactions", description = "Endpoints for managing transactions")
@SecurityRequirement(name = "Bearer Token")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(
            summary = "Facilitates deposit transactions",
            description = "Accepts deposit amount, adds amount to current balance, displays updated balance"
    )
    @PostMapping ("/deposit")
    public ResponseEntity<ApiResponse> deposit(@RequestBody TransactionRequest transactionRequest){
        var depositRes= transactionService.deposit(transactionRequest);
        return new ResponseEntity<>(depositRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Facilitates withdraw transactions",
            description = "Accepts withdrawal amount, subtracts the amount from current balance, displays the updated balance"
    )
    @PostMapping ("/withdraw")
    public ResponseEntity<ApiResponse> withdraw(@RequestBody TransactionRequest transactionRequest){
        var withdrawRes= transactionService.withdraw(transactionRequest);
        return new ResponseEntity<>(withdrawRes, HttpStatus.OK);
    }

    @PostMapping("/isOverdraftOptedIn")
    public ResponseEntity<ApiResponse> isOverdraftOptedIn(@RequestBody TransactionRequest transactionRequest){
        var overdraftRes= transactionService.isOverdraftOptedIn(transactionRequest);
        return new ResponseEntity<>(overdraftRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Facilitates check balance transactions",
            description = "Allows user to see current balance"
    )
    @PostMapping("/checkBalance")
    public ResponseEntity<ApiResponse> checkBalance(@RequestBody BalanceRequest balanceRequest){
        var checkBalanceRes= transactionService.checkBalance(balanceRequest);
        return new ResponseEntity<>(checkBalanceRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Facilitates transfer of funds between accounts",
            description = "Allows user to input the "
    )
    @PostMapping ("/transferFunds")
    public ResponseEntity<ApiResponse> transferFunds(@RequestBody TransferFundsRequest transferFundsRequest){
        var transferFundsRes= transactionService.transferFunds(transferFundsRequest);
        return new ResponseEntity<>(transferFundsRes, HttpStatus.OK);
    }
}
