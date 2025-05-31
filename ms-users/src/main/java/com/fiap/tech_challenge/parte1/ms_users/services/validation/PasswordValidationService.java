package com.fiap.tech_challenge.parte1.ms_users.services.validation;

import com.fiap.tech_challenge.parte1.ms_users.exceptions.InvalidPasswordException;
import com.fiap.tech_challenge.parte1.ms_users.validators.PasswordValidator;
import org.springframework.stereotype.Component;

/**
 * Service responsible for validating password change requests.
 * It ensures that the old password matches and that the new password
 * is different from the old one.
 */
@Component
public class PasswordValidationService implements PasswordValidator {

    /**
     * Validates the password change criteria.
     *
     * @param oldPasswordMatches boolean indicating if the old password provided matches the current password
     * @param isSameAsOld        boolean indicating if the new password is the same as the old password
     * @throws InvalidPasswordException if the old password does not match or if the new password is the same as the old one
     */
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
