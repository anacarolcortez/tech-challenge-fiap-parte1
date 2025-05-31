package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;

/**
 * Interface for validating user data in a {@link UsersRequestDTO}.
 * <p>
 * Implementations of this interface should define specific validation rules
 * for user registration or update requests.
 */
public interface UserValidator {

    /**
     * Validates the provided {@link UsersRequestDTO}.
     *
     * @param dto the user data transfer object containing the information to be validated.
     * @throws RuntimeException or a specific validation exception if the data is invalid.
     */
    void validate(UsersRequestDTO dto);
}
