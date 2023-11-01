package com.cognixia.radiant.exceptions;

public class InvalidLoginException extends RuntimeException{

    public InvalidLoginException() {
        super("Invalid username or password. Please try again.");
    }
}
