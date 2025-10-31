package org.khanguhizi.bankmanagementsystem.exceptionhandler;

import org.khanguhizi.bankmanagementsystem.dto.ApiResponse;
import org.khanguhizi.bankmanagementsystem.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(InvalidEntryException.class)
    public ResponseEntity<ApiResponse> handleInvalidEntryException(InvalidEntryException invalidEntryException) {
        ApiResponse response = ApiResponse.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(invalidEntryException.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Bad Request");
        body.put("message", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Not Found");
        body.put("message", "The requested endpoint does not exist");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Method Not Allowed");
        body.put("message", "HTTP method not supported for this endpoint");
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedAccessException(UnauthorizedAccessException unauthorizedAccessException) {
        ApiResponse response = ApiResponse.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .message(unauthorizedAccessException.getMessage())
                .build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
