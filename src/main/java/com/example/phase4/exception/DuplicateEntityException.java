package com.example.phase4.exception;

import jakarta.persistence.PersistenceException;

public class DuplicateEntityException extends PersistenceException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
