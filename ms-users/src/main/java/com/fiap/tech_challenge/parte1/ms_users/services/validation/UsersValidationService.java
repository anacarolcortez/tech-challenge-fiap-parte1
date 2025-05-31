package com.fiap.tech_challenge.parte1.ms_users.services.validation;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.validators.UserValidator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Service responsible for validating user data during creation or update.
 * It delegates the validation process to a list of UserValidator implementations.
 */
@Component
public class UsersValidationService {

    private final List<UserValidator> userCreationValidators;

    /**
     * Constructs a UsersValidationService with a list of UserValidator instances.
     *
     * @param userCreationValidators List of UserValidator implementations to apply during validation
     */
    public UsersValidationService(List<UserValidator> userCreationValidators) {
        this.userCreationValidators = userCreationValidators;
    }

    /**
     * Runs all registered UserValidators to validate the provided UsersRequestDTO.
     *
     * @param dto the user data transfer object containing user information to validate
     */
    public void validateAll(UsersRequestDTO dto) {
        for (UserValidator validator : userCreationValidators) {
            validator.validate(dto);
        }
    }

}
