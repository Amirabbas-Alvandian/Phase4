package com.example.phase4.exception;

public class CaptchaRevokedException extends RuntimeException{
    public CaptchaRevokedException(String message) {
        super(message);
    }
}
