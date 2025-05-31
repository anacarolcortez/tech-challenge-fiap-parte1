package com.fiap.tech_challenge.parte1.ms_users.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DTO to update user information including addresses.
 *
 * @param name    the user's name (required)
 * @param email   the user's email (required and must be valid)
 * @param login   the user's login (required)
 * @param address list of addresses (must have at least one and each address is validated)
 */
public record UpdateUserDTO(
        @NotBlank(message = "User field 'name' is required")
        String name,

        @NotBlank(message = "User field 'email' is required")
        @Email(message = "User field 'email' must be a valid email address")
        String email,

        @NotBlank(message = "User field 'login' is required")
        String login,

        @Valid
        @NotEmpty(message = "User must have at least one address")
        List<AddressRequestDTO> address
) {}
