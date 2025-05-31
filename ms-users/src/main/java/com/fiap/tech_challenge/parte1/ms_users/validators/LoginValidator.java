package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.LoginAlreadyExistsException;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Validator that checks if a given login is already registered in the system.
 * <p>
 * Implements the {@link UserValidator} interface and uses {@link UserRepository} to verify login uniqueness.
 */
@Component
public class LoginValidator implements UserValidator {

    private final UserRepository userRepository;

    /**
     * Constructs a {@code LoginValidator} with the given {@link UserRepository}.
     *
     * @param userRepository the repository used to check if the login already exists.
     */
    public LoginValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates whether the login in the given {@link UsersRequestDTO} already exists.
     *
     * @param dto the DTO containing the user's login to validate.
     * @throws LoginAlreadyExistsException if the login is already registered in the system.
     */
    @Override
    public void validate(UsersRequestDTO dto) {
        if (userRepository.existsByLogin(dto.login())) {
            throw new LoginAlreadyExistsException("The provided login is already in use.");
        }
    }
}
