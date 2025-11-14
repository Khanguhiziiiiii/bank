package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

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
    public ApiResponse getAllCustomers(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); //Creates a Spring Data Pageable object to tell the repository which page and how many records per page you want.
        Page<Customer> customerPage; //Declares a variable that will hold the paged list of customers returned by the repository.

        if (StringUtils.hasText(search)) { //Checks whether the search string is not null and not empty.
            customerPage = customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, search, pageable);
            /*
            Calls a custom query method in your CustomerRepository.
            This method automatically searches (case-insensitive) for customers whose:
            First name contains the search value OR
            Last name contains the search value OR
            Email contains the search value
            The results are paginated using the Pageable object.
             */
        } else {
            customerPage = customerRepository.findAll(pageable);
            //If no search text was provided, just return all customers paginated.
        }

        return ApiResponse.builder()
                .message("Customers fetched successfully")
                .data(customerPage)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse updateCustomer(Integer id, Customer updatedData) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoAccountsFoundException("Customer not found"));

        customer.setFirstName(updatedData.getFirstName());
        customer.setLastName(updatedData.getLastName());
        customer.setEmail(updatedData.getEmail());
        customer.setPhoneNumber(updatedData.getPhoneNumber());
        customer.setNationalId(updatedData.getNationalId());

        customerRepository.save(customer);

        return ApiResponse.builder()
                .message("Customer updated successfully")
                .data(customer)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }


    public ApiResponse softDeleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoAccountsFoundException("Customer not found"));
        customer.setDeleted(true);
        customerRepository.save(customer);

        return ApiResponse.builder()
                .message("Customer deleted (soft) successfully")
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse toggleBlockCustomer(Integer id, boolean block) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NoAccountsFoundException("Customer not found"));

        customer.setBlocked(block);
        customerRepository.save(customer);

        String msg = block ? "Customer blocked successfully" : "Customer unblocked successfully";
        return ApiResponse.builder()
                .message(msg)
                .data(customer)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse getAllAccounts(Integer customerId) {
        List<Accounts> accounts = accountRepository.findByCustomerId(customerId);
        return ApiResponse.builder()
                .message("Accounts fetched successfully")
                .data(accounts)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse getAccountDetails(Integer accountId) {
        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoAccountsFoundException("Account not found"));
        return ApiResponse.builder()
                .message("Account details fetched")
                .data(account)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse updateAccountStatus(Integer accountId, boolean active) {
        Accounts account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoAccountsFoundException("Account not found"));
        account.setActive(active);
        accountRepository.save(account);

        return ApiResponse.builder()
                .message(active ? "Account activated" : "Account deactivated")
                .data(account)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse getAllTransactions(String type, Integer customerId) {
        List<Transactions> transactions;

        if (StringUtils.hasText(type)) {
            transactions = transactionRepository.findByTransactionTypeIgnoreCase(type);
        } else if (customerId != null) {
            transactions = transactionRepository.findByAccount_Customer_Id(customerId);
        } else {
            transactions = transactionRepository.findAll();
        }

        return ApiResponse.builder()
                .message("Transactions fetched successfully")
                .data(transactions)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse getTransactionDetails(Integer transactionId) {
        Transactions transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoTransactionsFoundException("Transaction not found"));

        return ApiResponse.builder()
                .message("Transaction details fetched")
                .data(transaction)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }
}
