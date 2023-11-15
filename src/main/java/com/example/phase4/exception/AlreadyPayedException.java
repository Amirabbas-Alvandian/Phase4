package com.example.phase4.exception;

public class AlreadyPayedException extends RuntimeException{
    public AlreadyPayedException(String message) {
        super(message);
    }
}
