package com.fiap.tech_challenge.parte1.ms_users.validators;

public interface PasswordValidator {
    void validate(boolean oldPasswordMatches, boolean isSameAsOld);
}
