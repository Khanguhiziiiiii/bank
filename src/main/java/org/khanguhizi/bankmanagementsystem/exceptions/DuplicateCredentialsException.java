package org.khanguhizi.bankmanagementsystem.exceptions;

public class DuplicateCredentialsException extends RuntimeException{
    public DuplicateCredentialsException(String message) {
        super(message);
    }
}
