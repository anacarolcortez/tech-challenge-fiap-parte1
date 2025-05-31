package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.EmailAlreadyExistsException;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Validator that checks if a given email address is already registered in the system.
 * <p>
 * Implements the {@link UserValidator} interface and queries the database using {@link UserRepository}.
 */
@Component
public class EmailValidator implements UserValidator {

    private final UserRepository userRepository;

    /**
     * Constructs an {@code EmailValidator} with the specified {@link UserRepository}.
     *
     * @param userRepository the repository used to check if the email already exists.
     */
    public EmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates whether the email in the given {@link UsersRequestDTO} already exists.
     *
     * @param dto the DTO containing the user's email to validate.
     * @throws EmailAlreadyExistsException if the email is already registered.
     */
    @Override
    public void validate(UsersRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("The provided email is already in use.");
        }
    }
}
