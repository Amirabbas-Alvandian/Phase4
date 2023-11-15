package com.example.phase4.exception;

public class InvalidCaptchaTextException extends RuntimeException{

    public InvalidCaptchaTextException(String message) {
        super(message);
    }
}
