package org.khanguhizi.bankmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.models.TransactionTypes;
import org.khanguhizi.bankmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(originPatterns = "*", origins = {"*"}, allowedHeaders = {"*"})

@RestController
@Tag(name = "Transactions", description = "Endpoints for managing transactions")
@SecurityRequirement(name = "Bearer Token")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionCostsService transactionCostsService;

    @Operation(
            summary = "Facilitates deposit transactions",
            description = "Accepts deposit amount, adds amount to current balance, displays updated balance"
    )
@PreAuthorize("hasRole('CREATE_TRANSACTION')")
    @PostMapping ("/deposit")
    public ResponseEntity<ApiResponse> deposit(@RequestBody TransactionRequest transactionRequest){
        var depositRes= transactionService.deposit(transactionRequest);
        return new ResponseEntity<>(depositRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Facilitates withdraw transactions",
            description = "Accepts withdrawal amount, subtracts the amount from current balance, displays the updated balance"
    )
@PreAuthorize("hasRole('CREATE_TRANSACTION')")
    @PostMapping ("/withdraw")
    public ResponseEntity<ApiResponse> withdraw(@RequestBody TransactionRequest transactionRequest){
        var withdrawRes= transactionService.withdraw(transactionRequest);
        return new ResponseEntity<>(withdrawRes, HttpStatus.OK);
    }

    //@PreAuthorize("hasRole('UPDATE_ACCOUNT')")
    //@PostMapping("/isOverdraftOptedIn")
    //public ResponseEntity<ApiResponse> isOverdraftOptedIn(@RequestBody TransactionRequest transactionRequest){
       // var overdraftRes= transactionService.isOverdraftOptedIn(transactionRequest);
        //return new ResponseEntity<>(overdraftRes, HttpStatus.OK);
    //}

    @Operation(
            summary = "Facilitates check balance transactions",
            description = "Allows user to see current balance"
    )
@PreAuthorize("hasRole('READ_ACCOUNT')")
    @PostMapping("/checkBalance")
    public ResponseEntity<ApiResponse> checkBalance(@RequestBody BalanceRequest balanceRequest){
        var checkBalanceRes= transactionService.checkBalance(balanceRequest);
        return new ResponseEntity<>(checkBalanceRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Facilitates transfer of funds between accounts",
            description = "Allows user to input the recipient account and amount to send, then transfer the funds to the recipient account"
    )
@PreAuthorize("hasRole('CREATE_TRANSACTION')")
    @PostMapping ("/transferFunds")
    public ResponseEntity<ApiResponse> transferFunds(@RequestBody TransferFundsRequest transferFundsRequest){
        var transferFundsRes= transactionService.transferFunds(transferFundsRequest);
        return new ResponseEntity<>(transferFundsRes, HttpStatus.OK);
    }

    @Operation(
            summary = "Fetch account statement",
            description = "Retrieves all transactions for a specific account, similar to a bank statement"
    )
    @PreAuthorize("hasRole('READ_ACCOUNT')")
    @GetMapping("/account/{accountNumber}/statement")
    public ResponseEntity<ApiResponse> getAccountStatement(@PathVariable String accountNumber) {

        var statementRes = transactionService.getAccountStatement(accountNumber);
        return ResponseEntity.ok(statementRes);
    }

    @Operation(
            summary = "Manage transaction costs",
            description = "Allows admin to set and update transaction costs"
    )
@PreAuthorize("hasRole('UPDATE_TRANSACTION_COSTS')")
    @PostMapping("/admin/updateTransactionCosts")
    public ResponseEntity<ApiResponse> updateTransactionCosts(@RequestBody TransactionCostsRequest transactionCostsRequest){
        var updateTransactionCostRes =transactionCostsService.addCost(transactionCostsRequest);
        return new ResponseEntity<>(updateTransactionCostRes, HttpStatus.OK);
    }

    @Operation(
            summary = "previews transaction cost to be charged",
            description = "displays the transaction cost based on the type of transaction and amount to be transacted"
    )
@PreAuthorize("hasRole('READ_TRANSACTION_COSTS')")
    @GetMapping("/charges")
    public ResponseEntity<ApiResponse> getTransactionCharge(
            @RequestParam double amount,
            @RequestParam TransactionTypes type
    ) {
        var previewTransactions = transactionService.previewCharge(amount, type);
        return new ResponseEntity<>(previewTransactions, HttpStatus.OK);
    }
}
