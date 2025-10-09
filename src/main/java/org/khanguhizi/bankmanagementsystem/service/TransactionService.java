package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final AccountTypeRepository accountTypeRepository;


    private String generateTransactionCode(){
        String chars = "ABCDEFGHIJLMNOPQRSTUVWXYZ1234567890";
        Random random = new Random();
        StringBuilder transactionCode = new StringBuilder();
        for (int i = 0; i < 10; i++){
            int index = (int) (random.nextDouble() * chars.length());
            transactionCode.append(chars.charAt(index));
        }
        return transactionCode.toString();
    }

    public ApiResponse deposit(TransactionRequest request) {
        Optional<Accounts> existingAccount = accountRepository.findById(request.getAccountId());
        if (existingAccount.isEmpty()) {
            throw new NoAccountsFoundException("Account not found!");
        }

        Accounts account = existingAccount.get();
        double amount = request.getAmount();


        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);


        String transactionCode = generateTransactionCode();


        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                .account(account)
                .balance(account.getBalance())
                .transactionType("deposit")
                .build();

        transactionRepository.save(transaction);


        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionCode(transactionCode);
        transactionResponse.setBalance(account.getBalance());

        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(transactionResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse withdraw(TransactionRequest request) {
        Optional<Accounts> existingAccount = accountRepository.findById(request.getAccountId());
        if (existingAccount.isEmpty()) {
            throw new NoAccountsFoundException("Account not found!");
        }

        Accounts account = existingAccount.get();
        double amount = request.getAmount();

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        String transactionCode = generateTransactionCode();


        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                .account(account)
                .balance(account.getBalance())
                .transactionType("withdrawal")
                .build();

        transactionRepository.save(transaction);


        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionCode(transactionCode);
        transactionResponse.setBalance(account.getBalance());

        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(transactionResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}
