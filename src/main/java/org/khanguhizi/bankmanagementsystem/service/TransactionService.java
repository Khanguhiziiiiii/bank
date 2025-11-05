package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import org.khanguhizi.bankmanagementsystem.utilities.SecurityUtility;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionCostsService transactionCostsService;
    private final SecurityUtility securityUtility;


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
        Optional<Accounts> existingAccount = accountRepository.findByAccountNumber(request.getFromAccount());
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
        securityUtility.verifyAccountOwnership(request.getToAccount());

        Optional<Accounts> existingAccount = accountRepository.findByAccountNumber(request.getToAccount());
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

        double transactionCost = transactionCostsService.getTransactionCost(amount, TransactionTypes.DEPOSIT);

        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                .toAccount(account.getAccountNumber())
                .amount(amount)
                .toBalance(account.getBalance())
                .transactionCost(transactionCost)
                .transactionType(TransactionTypes.DEPOSIT.name())
                .fromAccount("N/A")
                .fromBalance(0)
                .build();

        transactionRepository.save(transaction);


        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionCode(transactionCode);
        transactionResponse.setToBalance(account.getBalance());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setTransactionCost(transactionCost);


        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(transactionResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse withdraw(TransactionRequest request) {
        securityUtility.verifyAccountOwnership(request.getFromAccount());

        Optional<Accounts> existingAccount = accountRepository.findByAccountNumber(request.getFromAccount());
        if (existingAccount.isEmpty()) {
            throw new NoAccountsFoundException("Account not found!");
        }

        Accounts account = existingAccount.get();
        double amount = request.getAmount();

        Integer accountTypeId = account.getAccountType().getId();

        boolean isOverdraftOptedIn = request.isOverdraftOptedIn();

        double transactionCost = transactionCostsService.getTransactionCost(amount, TransactionTypes.WITHDRAW);

        if(amount<=0){
            throw new InvalidEntryException("Enter valid amount!");
        }

        if (accountTypeId == 1) {
            //log.info("accountypeid: {}" ,accountTypeId);
            if (isOverdraftOptedIn) {
                double overdraftLimit = account.getBalance() * 0.10;
                double maxAllowedWithdrawal = account.getBalance() + overdraftLimit;
                if (amount + transactionCost > maxAllowedWithdrawal) {
                    throw new InsufficientFundsException("Insufficient funds! Maximum withdrawal is " + maxAllowedWithdrawal);
                }
            }else{
                if (account.getBalance() < amount + transactionCost) {
                    throw new InsufficientFundsException("Insufficient Funds!");
                }
            }
        } else {
            if (account.getBalance() < amount + transactionCost) {
                throw new InsufficientFundsException("Insufficient Funds!");
            }
        }

        if (accountTypeId == 3) {
            double maxAllowedWithdrawal = account.getBalance() * 0.10;
            if (amount + transactionCost> maxAllowedWithdrawal) {
                throw new InvalidEntryException("Amount exceeds what is allowed! Maximum deposit is " + maxAllowedWithdrawal);
            }
        }

        account.setBalance(account.getBalance() - amount - transactionCost);
        accountRepository.save(account);

        String transactionCode = generateTransactionCode();


        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                .fromAccount(account.getAccountNumber())
                .amount(amount)
                .fromBalance(account.getBalance())
                .transactionCost(transactionCost)
                .transactionType(TransactionTypes.WITHDRAW.name())
                .toAccount("N/A")
                .toBalance(0)
                .build();

        transactionRepository.save(transaction);


        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionCode(transactionCode);
        transactionResponse.setFromBalance(account.getBalance());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setTransactionCost(transactionCost);

        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(transactionResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse checkBalance(BalanceRequest request) {
        securityUtility.verifyAccountOwnership(request.getFromAccount());
        Optional<Accounts> existingAccount = accountRepository.findByAccountNumber(request.getFromAccount());
        if (existingAccount.isEmpty()) {
            throw new NoAccountsFoundException("Account not found!");
        }

        Accounts account = existingAccount.get();

        String transactionCode = generateTransactionCode();

        Integer accountTypeId = account.getAccountType().getId();

        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setFromAccount(account.getAccountNumber());
        balanceResponse.setAccountType(accountTypeId);
        balanceResponse.setTransactionCode(transactionCode);
        balanceResponse.setFromBalance(account.getBalance());

        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(balanceResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse transferFunds(TransferFundsRequest request){
        securityUtility.verifyAccountOwnership(request.getFromAccount());

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

        double transactionCost = transactionCostsService.getTransactionCost(amount, TransactionTypes.TRANSFER);

        if (request.getAmount() <= 0){
            throw new InvalidEntryException("Enter valid amount!");
        }

        Integer accountTypeId = fromAccount.getAccountType().getId();
        boolean isOverdraftOptedIn = fromAccount.isOverdraftOptedIn();

        double maxAllowedTransfer = fromAccount.getBalance() + transactionCost;
        if (accountTypeId == 1 && isOverdraftOptedIn) {
            maxAllowedTransfer += fromAccount.getBalance() * 0.10;
        }

        if (accountTypeId == 3){
            maxAllowedTransfer = fromAccount.getBalance() * 0.10;
        }

        if (amount>maxAllowedTransfer){
            throw new InsufficientFundsException("Insufficient Funds!");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount - transactionCost);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String transactionCode = generateTransactionCode();

        Transactions transaction = Transactions.builder()
                .transactionCode(transactionCode)
                .fromAccount(fromAccount.getAccountNumber())
                .fromBalance(fromAccount.getBalance())
                .amount(amount)
                .transactionCost(transactionCost)
                .transactionType(TransactionTypes.TRANSFER.name())
                .toAccount(toAccount.getAccountNumber())
                .toBalance(toAccount.getBalance())
                .build();

        transactionRepository.save(transaction);

        TransferFundsResponse  transferFundsResponse = new TransferFundsResponse();
        transferFundsResponse.setTransactionCode(transactionCode);
        transferFundsResponse.setFromAccount(fromAccount.getAccountNumber());
        transferFundsResponse.setFromBalance(fromAccount.getBalance());
        transferFundsResponse.setAmount(amount);
        transferFundsResponse.setToAccount(toAccount.getAccountNumber());
        transferFundsResponse.setToBalance(toAccount.getBalance());
        transferFundsResponse.setTransactionCost(transactionCost);


        return ApiResponse.builder()
                .message("Transaction Successful!")
                .data(transferFundsResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse getAccountStatement(String accountNumber){
        securityUtility.verifyAccountOwnership(accountNumber);

        Optional<Accounts> existingAccount = accountRepository.findByAccountNumber(accountNumber);
        if(existingAccount.isEmpty()){
            throw new NoAccountsFoundException("Account not found!");
        }

        List<Transactions> transactions = transactionRepository
                .findByFromAccountOrToAccountOrderByTransactionDate(accountNumber, accountNumber);

        if(transactions.isEmpty()){
            throw new NoTransactionsFoundException("No transactions for this account!");
        }

        List<TransactionResponse> statement = transactions.stream().map(tx -> {
            TransactionResponse transactionResponse = new TransactionResponse();
            transactionResponse.setTransactionCode(tx.getTransactionCode());
            transactionResponse.setTransactionType(tx.getTransactionType());
            transactionResponse.setFromAccount(tx.getFromAccount());
            transactionResponse.setToAccount(tx.getToAccount());
            transactionResponse.setAmount(tx.getAmount() == null ? 0.0 : tx.getAmount());
            transactionResponse.setDate(tx.getTransactionDate());
            return transactionResponse;
        }).toList();

        return ApiResponse.builder()
                .message("Account statement retrieved successfully.")
                .data(statement)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}
