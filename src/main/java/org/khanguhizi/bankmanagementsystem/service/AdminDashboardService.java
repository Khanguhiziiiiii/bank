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
        List<Customer> customers = customerRepository.findAll();
        List<Accounts> accounts = accountRepository.findAll();
        List<Transactions> transactions = transactionRepository.findAll();

        Map<String, List<Accounts>> groupedAccounts = accounts.stream()
                .collect(Collectors.groupingBy(acc ->{
                    Integer accountTypeId = acc.getAccountType().getId();
                    return switch (accountTypeId){
                        case 1 -> "Checking";
                        case 2 -> "Savings";
                        case 3 -> "Junior";
                        default -> "unknown";
                    };
                }));
        long totalCustomers = customers.size();
        long totalAccounts = accounts.size();
        long totalTransactions = transactions.size();

        double totalBankBalance = accounts.stream()
                .mapToDouble(Accounts::getBalance)
                .sum();

        double moneyIn = transactions.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("deposit"))
                .mapToDouble(Transactions::getAmount)
                .sum();

        double moneyOut = transactions.stream()
                .filter(t -> t.getTransactionType().equalsIgnoreCase("withdraw"))
                .mapToDouble(Transactions::getAmount)
                .sum();

        AdminDashboardResponse.Summary summary = AdminDashboardResponse.Summary.builder()
                .totalCustomers(totalCustomers)
                .totalAccounts(totalAccounts)
                .totalTransactions(totalTransactions)
                .bankBalance(totalBankBalance)
                .moneyIn(moneyIn)
                .moneyOut(moneyOut)
                .build();

        AdminDashboardResponse adminDashboardResponse = AdminDashboardResponse.builder()
                .customers(customers)
                .accounts(groupedAccounts)
                .transactions(transactions)
                .summary(summary)
                .build();

        return ApiResponse.builder()
                .message("Admin Dashboard Data Retrieved Successfully!")
                .data(adminDashboardResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}
