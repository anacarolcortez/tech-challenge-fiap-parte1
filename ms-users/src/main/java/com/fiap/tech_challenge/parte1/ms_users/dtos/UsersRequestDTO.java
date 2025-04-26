package com.fiap.tech_challenge.parte1.ms_users.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record UsersRequestDTO(
        @NotBlank(message = "User field 'name' is required")
        String name,

        @NotBlank(message = "User field 'email' is required")
        @Email(message = "User field 'email' must be a valid email address")
        String email,

        @NotBlank(message = "User field 'login' is required")
        String login,

        @NotBlank(message = "User field 'password' is required")
        String password,

        @NotBlank(message = "User field 'role' is required")
        @Pattern(regexp = "OWNER|CLIENT", message = "User field 'role' must be either 'OWNER' or 'CLIENT'")
        String role,

        @Valid
        @NotEmpty(message = "User must have at least one Address")
        List<AddressRequestDTO> address
) {
}
