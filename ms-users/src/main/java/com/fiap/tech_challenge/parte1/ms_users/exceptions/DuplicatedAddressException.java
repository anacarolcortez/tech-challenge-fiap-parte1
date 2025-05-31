package com.fiap.tech_challenge.parte1.ms_users.exceptions;

/**
 * Exception thrown when an attempt is made to add or register an address
 * that already exists (i.e., a duplicate address) in the system.
 */
public class DuplicatedAddressException extends RuntimeException {

    /**
     * Constructs a new DuplicatedAddressException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public DuplicatedAddressException(String message) {
        super(message);
    }
}
