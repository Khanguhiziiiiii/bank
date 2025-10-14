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

    public ApiResponse isOverdraftOptedIn(TransactionRequest request) {
        Optional<Accounts> existingAccount = accountRepository.findById(request.getAccountId());
        if (existingAccount.isEmpty()) {
            throw new NoAccountsFoundException("Account not found!");
        }

        Accounts account = existingAccount.get();

        Integer accountTypeId = account.getAccountType().getId();

        if (accountTypeId != 1) {
            throw new InvalidEntryException("Only checking accounts can opt into overdraft.");
        }

        if (request.isOverdraftOptedIn()) {
            return ApiResponse.builder()
                    .message("Already opted into overdraft.")
                    .data(true)
                    .status(String.valueOf(HttpStatus.OK))
                    .build();
        }

        account.setOverdraftOptedIn(true);
        accountRepository.save(account);

        return ApiResponse.builder()
                .message("Successfully opted into overdraft.")
                .data(true)
                .status(String.valueOf(HttpStatus.OK))
                .build();
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

        Integer accountTypeId = account.getAccountType().getId();

        boolean isOverdraftOptedIn = request.isOverdraftOptedIn();

        if(amount<0){
            throw new InvalidEntryException("Enter valid amount!");
        }

        if (accountTypeId == 1) {
            log.info("accountypeid: {}" ,accountTypeId);
            if (isOverdraftOptedIn) {
                double overdraftLimit = account.getBalance() * 0.10;
                double maxWithdrawal = account.getBalance() + overdraftLimit;
                if (amount > maxWithdrawal) {
                    throw new InsufficientFundsException("Insufficient funds! Maximum withdrawal is " + maxWithdrawal);
                }
            }else{
                if (account.getBalance() < amount) {
                    throw new InsufficientFundsException("Insufficient Funds!");
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
