package org.khanguhizi.bankmanagementsystem.exceptions;

public class NoTransactionsFoundException extends RuntimeException {
    public NoTransactionsFoundException(String message) {
        super(message);
    }
}
