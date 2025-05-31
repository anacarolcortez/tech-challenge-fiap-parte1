package com.fiap.tech_challenge.parte1.ms_users.dtos;

/**
 * Data Transfer Object used as response after a user creation.
 * Contains the created user information and the JWT token for authentication.
 *
 * @param user the details of the created user
 * @param tokenJWT the JWT token issued for the user session
 */
public record CreateUserDTO(
        UsersResponseDTO user,
        String tokenJWT
) {
}
