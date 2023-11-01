package com.cognixia.radiant.exceptions;

public class InvalidLoginException extends RuntimeException{

    public InvalidLoginException() {
        super("Either username or password was incorrect. Please try again.");
    }
}
