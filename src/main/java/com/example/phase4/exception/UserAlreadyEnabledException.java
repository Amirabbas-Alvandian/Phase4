package com.example.phase4.exception;

public class UserAlreadyEnabledException extends RuntimeException{
    public UserAlreadyEnabledException(String message) {
        super(message);
    }
}
