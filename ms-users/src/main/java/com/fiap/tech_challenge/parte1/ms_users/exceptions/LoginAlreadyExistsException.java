package com.fiap.tech_challenge.parte1.ms_users.exceptions;

/**
 * Exception thrown when an attempt is made to register or use a login
 * that already exists in the system.
 */
public class LoginAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new LoginAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message explaining the login conflict
     */
    public LoginAlreadyExistsException(String message) {
        super(message);
    }
}
