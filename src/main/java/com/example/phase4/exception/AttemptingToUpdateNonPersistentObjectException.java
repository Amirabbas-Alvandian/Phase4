package com.example.phase4.exception;

public class AttemptingToUpdateNonPersistentObjectException extends RuntimeException{

    public AttemptingToUpdateNonPersistentObjectException(String message) {
        super(message);
    }
}
