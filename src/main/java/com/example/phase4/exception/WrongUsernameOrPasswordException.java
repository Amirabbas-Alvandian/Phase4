package com.example.phase4.exception;

public class WrongUsernameOrPasswordException extends RuntimeException{
    public WrongUsernameOrPasswordException(String message) {
        super(message);
    }
}
