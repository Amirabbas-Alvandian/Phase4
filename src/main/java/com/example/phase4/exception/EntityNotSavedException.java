package com.example.phase4.exception;

public class EntityNotSavedException extends RuntimeException{
    public EntityNotSavedException(String message) {
        super(message);
    }
}
