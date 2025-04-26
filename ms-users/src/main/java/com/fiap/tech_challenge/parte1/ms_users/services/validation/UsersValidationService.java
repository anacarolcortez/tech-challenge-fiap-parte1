package com.fiap.tech_challenge.parte1.ms_users.services.validation;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.validators.UserValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersValidationService {

    private final List<UserValidator> validators;

    public UsersValidationService(List<UserValidator> validators) {
        this.validators = validators;
    }

    public void validateAll(UsersRequestDTO dto) {
        for (UserValidator validator : validators) {
            validator.validate(dto);
        }
    }
}
