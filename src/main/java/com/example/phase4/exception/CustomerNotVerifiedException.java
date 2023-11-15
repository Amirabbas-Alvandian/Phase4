package com.example.phase4.exception;

public class CustomerNotVerifiedException extends RuntimeException{
    public CustomerNotVerifiedException(String message) {
        super(message);
    }
}
