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

        if(amount<=0){
            throw new InvalidEntryException("Enter valid amount!");
        }

        account.setBalance(account.getBalance()+amount);
        accountRepository.save(account);


        String transactionCode = generateTransactionCode();


        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                .account(account)
                .amount(amount)
                .balance(account.getBalance())
                .transactionType("deposit")
                .fromAccount("N/A")
                .toAccount("N/A")
                .build();

        transactionRepository.save(transaction);


        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionCode(transactionCode);
        transactionResponse.setBalance(account.getBalance());
        transactionResponse.setAmount(transaction.getAmount());


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

        if(amount<=0){
            throw new InvalidEntryException("Enter valid amount!");
        }

        if (accountTypeId == 1) {
            log.info("accountypeid: {}" ,accountTypeId);
            if (isOverdraftOptedIn) {
                double overdraftLimit = account.getBalance() * 0.10;
                double maxAllowedWithdrawal = account.getBalance() + overdraftLimit;
                if (amount > maxAllowedWithdrawal) {
                    throw new InsufficientFundsException("Insufficient funds! Maximum withdrawal is " + maxAllowedWithdrawal);
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
            double maxAllowedWithdrawal = account.getBalance() * 0.10;
            if (amount > maxAllowedWithdrawal) {
                throw new InvalidEntryException("Amount exceeds what is allowed! Maximum deposit is " + maxAllowedWithdrawal);
            }
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        String transactionCode = generateTransactionCode();


        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                .account(account)
                .amount(amount)
                .balance(account.getBalance())
                .transactionType("withdrawal")
                .fromAccount("N/A")
                .toAccount("N/A")
                .build();

        transactionRepository.save(transaction);


        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionCode(transactionCode);
        transactionResponse.setBalance(account.getBalance());
        transactionResponse.setAmount(transaction.getAmount());

        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(transactionResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse checkBalance(BalanceRequest request) {
        Optional<Accounts> existingAccount = accountRepository.findById(request.getAccountId());
        if (existingAccount.isEmpty()) {
            throw new NoAccountsFoundException("Account not found!");
        }

        Accounts account = existingAccount.get();

        String transactionCode = generateTransactionCode();

        Integer accountTypeId = account.getAccountType().getId();

        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                .account(account)
                .balance(account.getBalance())
                .transactionType("balance")
                .build();

        transactionRepository.save(transaction);


        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setAccountNumber(account.getAccountNumber());
        balanceResponse.setAccountType(accountTypeId);
        balanceResponse.setTransactionCode(transactionCode);
        balanceResponse.setBalance(account.getBalance());

        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(balanceResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse transferFunds(TransferFundsRequest request){
        Optional<Accounts> existingFromAccount = accountRepository.findByAccountNumber(request.getFromAccount());
        if(existingFromAccount.isEmpty()){
            throw new NoAccountsFoundException("Sender Account not found!");
        }
        Accounts fromAccount = existingFromAccount.get();

        Optional<Accounts> existingToAccount = accountRepository.findByAccountNumber(request.getToAccount());
        if(existingToAccount.isEmpty()){
            throw new NoAccountsFoundException("Recipient Account not found!");
        }
        Accounts toAccount = existingToAccount.get();

        double amount = request.getAmount();

        if (request.getAmount() <= 0){
            throw new InvalidEntryException("Enter valid amount!");
        }

        Integer accountTypeId = fromAccount.getAccountType().getId();
        boolean isOverdraftOptedIn = fromAccount.isOverdraftOptedIn();

        double maxAllowedTransfer = fromAccount.getBalance();
        if (accountTypeId == 1 && isOverdraftOptedIn) {
            maxAllowedTransfer += fromAccount.getBalance() * 0.10;
        }

        if (accountTypeId == 3){
            maxAllowedTransfer = fromAccount.getBalance() * 0.10;
        }

        if (amount>maxAllowedTransfer){
            throw new InsufficientFundsException("Insufficient Funds!");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String transactionCode = generateTransactionCode();

        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                //.fromAccount(transaction.getFromAccount())
                .balance(fromAccount.getBalance())
                .transactionType("transfer")
                //.toAccount(transaction.getToAccount())
                .balance(toAccount.getBalance())
                .build();

        transactionRepository.save(transaction);

        TransferFundsResponse  transferFundsResponse = new TransferFundsResponse();
        transferFundsResponse.setTransactionCode(transactionCode);
        transferFundsResponse.setFromAccount(fromAccount.getAccountNumber());
        transferFundsResponse.setBalance(fromAccount.getBalance());
        transferFundsResponse.setToAccount(toAccount.getAccountNumber());
        transferFundsResponse.setBalance(toAccount.getBalance());

        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(transferFundsResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

}
