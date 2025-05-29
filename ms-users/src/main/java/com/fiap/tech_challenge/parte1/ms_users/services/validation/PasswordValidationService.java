package com.fiap.tech_challenge.parte1.ms_users.services.validation;

import com.fiap.tech_challenge.parte1.ms_users.exceptions.InvalidPasswordException;
import com.fiap.tech_challenge.parte1.ms_users.validators.PasswordValidator;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidationService implements PasswordValidator {

    @Override
    public void validate(boolean oldPasswordMatches, boolean isSameAsOld) {
        if (!oldPasswordMatches) {
            throw new InvalidPasswordException("Senha atual não confere");
        }
        if (isSameAsOld) {
            throw new InvalidPasswordException("Nova senha deve ser diferente da senha antiga");
        }
    }
}

