package com.example.phase4.exception;

public class AlreadySetFinishedException extends RuntimeException {
    public AlreadySetFinishedException(String message) {
        super(message);
    }
}
