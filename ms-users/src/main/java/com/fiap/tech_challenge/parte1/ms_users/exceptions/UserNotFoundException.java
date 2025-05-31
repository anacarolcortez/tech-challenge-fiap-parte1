package com.fiap.tech_challenge.parte1.ms_users.exceptions;

/**
 * Exception thrown when a requested user is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
