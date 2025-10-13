package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


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

        if(amount<0){
            throw new InvalidEntryException("Enter valid amount!");
        }

        account.setBalance(100);
        log.info("After updating the account balance  {}", account);

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

        Integer accountTypeId = request.getAccountTypeId();

        if(amount<0){
            throw new InvalidEntryException("Enter valid amount!");
        }

        if (accountTypeId == 1) {
            if (request.isOverdraftOptedIn()) {
                double overdraftLimit = account.getBalance() * 0.10;
                double maxWithdrawal = account.getBalance() + overdraftLimit;
                if (amount > maxWithdrawal) {
                    throw new InsufficientFundsException("Insufficient funds! Maximum withdrawal is " + maxWithdrawal);
                }
            }
        } else {
            if (account.getBalance() < amount) {
                throw new InsufficientFundsException("Insufficient Funds!");
            }
        }

        if (accountTypeId == 3) {
            double maxAllowed = account.getBalance() * 0.10;
            if (amount > maxAllowed) {
                throw new InvalidEntryException("Amount exceeds what is allowed! Maximum deposit is " + maxAllowed);
            }
        }

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
