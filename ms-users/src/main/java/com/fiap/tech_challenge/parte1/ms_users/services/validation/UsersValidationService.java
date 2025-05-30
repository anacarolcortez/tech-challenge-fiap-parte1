package com.fiap.tech_challenge.parte1.ms_users.services.validation;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.validators.UserValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersValidationService {

    private final List<UserValidator> userCreationValidators;

    public UsersValidationService(List<UserValidator> userCreationValidators) {
        this.userCreationValidators = userCreationValidators;
    }

    public void validateAll(UsersRequestDTO dto) {
        for (UserValidator validator : userCreationValidators) {
            validator.validate(dto);
        }
    }

}
