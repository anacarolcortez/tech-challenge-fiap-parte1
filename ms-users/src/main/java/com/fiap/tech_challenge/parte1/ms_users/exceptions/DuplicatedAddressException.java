package com.fiap.tech_challenge.parte1.ms_users.exceptions;

public class DuplicatedAddressException extends RuntimeException {
    public DuplicatedAddressException(String message) {
        super(message);
    }
}
