package com.fiap.tech_challenge.parte1.ms_users.services.validation;

import com.fiap.tech_challenge.parte1.ms_users.dtos.ChangePasswordRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.InvalidPasswordException;
import com.fiap.tech_challenge.parte1.ms_users.validators.PasswordValidator;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidationService implements PasswordValidator {

    @Override
    public void validate(ChangePasswordRequestDTO dto, String oldPassword) {
        if (!dto.oldPassword().equals(oldPassword)){
            throw new InvalidPasswordException("Senha atual não confere");
        }
        if (dto.newPassword().equals(dto.oldPassword())){
            throw new InvalidPasswordException("Nova senha deve ser diferente da senha antiga");
        }
    }
}

