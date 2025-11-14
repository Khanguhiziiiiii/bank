package org.khanguhizi.bankmanagementsystem.service;


import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;

    private String generateAccountNumber(){
        Random random = new Random();
        StringBuilder accNo = new StringBuilder();
        for (int i = 0; i < 10; i++){
            accNo.append(random.nextInt(10));
        }
        return accNo.toString();
    }

    public ApiResponse account(AccountRequest request){
        Optional<Customer> existingCustomer = customerRepository.findById(request.getCustomerId());
        if(existingCustomer.isEmpty()){
            throw new NoAccountsFoundException("Customer Not Found!");
        }

        Optional<AccountType> existingType =accountTypeRepository.findByAccountType(request.getAccountType());
        if(existingType.isEmpty()){
            throw new NoAccountsFoundException("Account Type Not Found!");
        }

        Customer customer = existingCustomer.get();
        AccountType accountType = existingType.get();

        String generatedAccountNumber = generateAccountNumber();

        Accounts account = Accounts.builder()
                .accountNumber(generatedAccountNumber)
                .customer(customer)
                .accountType(accountType)
                .balance(0.00)
                .build();

        accountRepository.save(account);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountNumber(generatedAccountNumber);
        accountResponse.setAccountType(request.getAccountType().toLowerCase());
        accountResponse.setBalance(0.00);


        return ApiResponse.builder()
                .message("Account Created Successfully!")
                .data(accountResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse fetchAccountsByCustomer(AccountRequest request) {
        Optional<Customer> existingCustomer = customerRepository.findById(request.getCustomerId());
        if (existingCustomer.isEmpty()) {
            throw new NoAccountsFoundException("Customer Not Found!");
        }

        List<Accounts> existingAccounts = accountRepository.findByCustomerId(request.getCustomerId());
        if (existingAccounts.isEmpty()) {
            throw new NoAccountsFoundException("No Accounts Found!");
        }

        List<AccountResponse> accountResponses = existingAccounts.stream().map(account -> {
            AccountResponse response = new AccountResponse();
            response.setAccountNumber(account.getAccountNumber());
            response.setAccountType(account.getAccountType().getAccountType().toLowerCase());
            response.setBalance(account.getBalance());
            return response;
        }).toList();

        return ApiResponse.builder()
                .message("Accounts Retrieved Successfully!")
                .data(accountResponses)
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
}
