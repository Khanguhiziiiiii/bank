package org.khanguhizi.bankmanagementsystem.controller;

import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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
    public ResponseEntity<ApiResponse> withdraw(@RequestBody TransactionRequest transactionRequest){
        var withdrawRes= transactionService.withdraw(transactionRequest);
        return new ResponseEntity<>(withdrawRes, HttpStatus.OK);
    }
}
