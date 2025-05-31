package com.fiap.tech_challenge.parte1.ms_users.exceptions;

/**
 * Exception thrown when JWT token validation fails.
 * This exception indicates that the token is invalid, expired, or cannot be verified.
 */
public class TokenValidationException extends RuntimeException {

    /**
     * Constructs a new TokenValidationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for token validation failure
     */
    public TokenValidationException(String message) {
        super(message);
    }
}
