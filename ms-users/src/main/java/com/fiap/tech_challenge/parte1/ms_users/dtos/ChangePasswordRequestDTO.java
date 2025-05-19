package com.fiap.tech_challenge.parte1.ms_users.dtos;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequestDTO(
        @NotBlank(message = "Field 'oldPassword' is required")
        String oldPassword,

        @NotBlank(message = "Field 'newPassword' is required")
        String newPassword
) {
}
