package org.khanguhizi.bankmanagementsystem.exceptionhandler;

import org.khanguhizi.bankmanagementsystem.dto.ApiResponse;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandlers {
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse> handleInvalidCredentials(InvalidCredentialsException invalidCredentialsException) {
        ApiResponse  response = ApiResponse.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(invalidCredentialsException.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateCredentialsException.class)
    public ResponseEntity<ApiResponse> handleDuplicateCredentials(DuplicateCredentialsException duplicateCredentialsException) {
        ApiResponse response = ApiResponse.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(duplicateCredentialsException.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAccountsFoundException.class)
    public ResponseEntity<ApiResponse> handleNoAccountsFound(NoAccountsFoundException noAccountsFoundException) {
        ApiResponse response = ApiResponse.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(noAccountsFoundException.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ApiResponse> handleDuplicateAccount(DuplicateAccountException duplicateAccountException) {
        ApiResponse response = ApiResponse.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(duplicateAccountException.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiResponse> handleInsufficientFundsException(InsufficientFundsException insufficientFundsException) {
        ApiResponse response = ApiResponse.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(insufficientFundsException.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
