package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final TransactionRepository transactionRepository;

    public ApiResponse getDashboardData() {
        long totalCustomers = customerRepository.count();
        List<Customer> customers = customerRepository.findAll();
        List<Accounts> accounts = accountRepository.findAll();
        List<Transactions> transactions = transactionRepository.findAll();

        Map<String, List<Accounts>> groupedAccounts = accounts.stream()
                .collect(Collectors.groupingBy(acc -> acc.getAccountType().getAccountType().toLowerCase()));

        long totalAccounts = accounts.size();
        long totalCheckingAccounts = accounts.stream()
                .filter(acc -> acc.getAccountType().getAccountType().equalsIgnoreCase("checking"))
                .count();
        long totalSavingsAccounts = accounts.stream()
                .filter(acc -> acc.getAccountType().getAccountType().equalsIgnoreCase("savings"))
                .count();
        long totalJuniorAccounts = accounts.stream()
                .filter(acc -> acc.getAccountType().getAccountType().equalsIgnoreCase("junior"))
                .count();

        long totalTransactions = transactions.size();
        long totalDepositTransactions = transactions.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("deposit"))
                .count();
        long totalWithdrawalTransactions = transactions.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("withdraw"))
                .count();
        long totalCheckBalanceTransactions = transactions.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("balance"))
                .count();
        long totalTransferTransactions = transactions.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("transfer"))
                .count();

        double bankBalance = accounts.stream().mapToDouble(Accounts::getBalance).sum();
        double moneyIn = transactions.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("deposit"))
                .mapToDouble(Transactions::getAmount)
                .sum();
        double moneyOut = transactions.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("withdraw"))
                .mapToDouble(Transactions::getAmount)
                .sum();

        AdminDashboardResponse.AccountsSummary accountsSummary = AdminDashboardResponse.AccountsSummary.builder()
                .total(totalAccounts)
                .checking(totalCheckingAccounts)
                .savings(totalSavingsAccounts)
                .junior(totalJuniorAccounts)
                .build();

        AdminDashboardResponse.TransactionsSummary transactionsSummary = AdminDashboardResponse.TransactionsSummary.builder()
                .total(totalTransactions)
                .deposit(totalDepositTransactions)
                .withdraw(totalWithdrawalTransactions)
                .transfer(totalTransferTransactions)
                .checkBalance(totalCheckBalanceTransactions)
                .build();

        AdminDashboardResponse.Summary summary = AdminDashboardResponse.Summary.builder()
                .totalCustomers(totalCustomers)
                .accounts(accountsSummary)
                .transactions(transactionsSummary)
                .bankBalance(bankBalance)
                .moneyIn(moneyIn)
                .moneyOut(moneyOut)
                .build();

        AdminDashboardResponse response = AdminDashboardResponse.builder()
                .customers(customers)
                .accounts(groupedAccounts)
                .transactions(transactions)
                .summary(summary)
                .build();

        return ApiResponse.builder()
                .message("Dashboard Data")
                .data(response)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}
