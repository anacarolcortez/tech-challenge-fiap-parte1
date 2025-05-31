package com.fiap.tech_challenge.parte1.ms_users.exceptions;

/**
 * Exception thrown when a provided password does not meet the
 * required validation criteria or is considered invalid.
 */
public class InvalidPasswordException extends RuntimeException {

    /**
     * Constructs a new InvalidPasswordException with the specified detail message.
     *
     * @param message the detail message explaining why the password is invalid
     */
    public InvalidPasswordException(String message) {
        super(message);
    }
}
