package org.khanguhizi.bankmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.khanguhizi.bankmanagementsystem.models.*;
import org.khanguhizi.bankmanagementsystem.dto.*;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.khanguhizi.bankmanagementsystem.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountTypeService {
    private final AccountTypeRepository accountTypeRepository;

    public final ApiResponse accountType(AccountTypeRequest request) {
        Optional<AccountType> accountType = accountTypeRepository.findByAccountType(request.getAccountType());
        if (accountType.isPresent()) {
            throw new DuplicateAccountException("Account Type Already Exists!");
        }

        AccountType type = AccountType.builder()
                .accountType(request.getAccountType())
                .build();

        accountTypeRepository.save(type);

        AccountTypeResponse accountTypeResponse = new AccountTypeResponse();
        accountTypeResponse.setAccountType(request.getAccountType().toLowerCase());


        return ApiResponse.builder()
                .message("Account Type Created Successfully!")
                .data(accountTypeResponse)
                .status(String.valueOf(HttpStatus.OK))
                .build();
    }

    public ApiResponse fetchAllAccountTypes() {
        List<AccountType> accountTypes = accountTypeRepository.findAll();

        if (accountTypes.isEmpty()) {
            throw new NoAccountsFoundException("No account types found!");
        }

        List<AccountTypeResponse> responses = accountTypes.stream()
                .map(type -> {
                    AccountTypeResponse res = new AccountTypeResponse();
                    res.setAccountType(type.getAccountType());
                    res.setAccountTypeId(type.getId());
                    return res;
                })
                .toList();

        return ApiResponse.builder()
                .message("Account Types Fetched Successfully!")
                .data(responses)
                .status(String.valueOf(HttpStatus.OK.value()))
                .build();
    }
}
