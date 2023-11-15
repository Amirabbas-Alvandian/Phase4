package com.example.phase4.exception;

public class InvalidImageSizeException extends RuntimeException{
    public InvalidImageSizeException(String message) {
        super(message);
    }
}
