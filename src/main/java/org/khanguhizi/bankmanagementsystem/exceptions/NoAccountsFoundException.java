package org.khanguhizi.bankmanagementsystem.exceptions;

public class NoAccountsFoundException extends RuntimeException {
    public NoAccountsFoundException(String message) {
        super(message);
    }
}
