package org.khanguhizi.bankmanagementsystem.controller;

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
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @PostMapping ("/deposit")
    public ResponseEntity<ApiResponse> deposit(@RequestBody TransactionRequest transactionRequest){
        var depositRes= transactionService.deposit(transactionRequest);
        return new ResponseEntity<>(depositRes, HttpStatus.OK);
    }

    @PostMapping ("/withdraw")
    @CrossOrigin("*")
    public ResponseEntity<ApiResponse> withdraw(@RequestBody TransactionRequest transactionRequest){
        var withdrawRes= transactionService.withdraw(transactionRequest);
        return new ResponseEntity<>(withdrawRes, HttpStatus.OK);
    }

    @PostMapping("/isOverdraftOptedIn")
    public ResponseEntity<ApiResponse> isOverdraftOptedIn(@RequestBody TransactionRequest transactionRequest){
        var overdraftRes= transactionService.isOverdraftOptedIn(transactionRequest);
        return new ResponseEntity<>(overdraftRes, HttpStatus.OK);
    }

    @PostMapping("/checkBalance")
    public ResponseEntity<ApiResponse> checkBalance(@RequestBody BalanceRequest balanceRequest){
        var checkBalanceRes= transactionService.checkBalance(balanceRequest);
        return new ResponseEntity<>(checkBalanceRes, HttpStatus.OK);
    }

    @PostMapping ("/transferFunds")
    public ResponseEntity<ApiResponse> transferFunds(@RequestBody TransferFundsRequest transferFundsRequest){
        var transferFundsRes= transactionService.transferFunds(transferFundsRequest);
        return new ResponseEntity<>(transferFundsRes, HttpStatus.OK);
    }
}
