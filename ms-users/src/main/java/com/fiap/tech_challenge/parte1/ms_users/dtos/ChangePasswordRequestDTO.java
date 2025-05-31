package com.fiap.tech_challenge.parte1.ms_users.dtos;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for password change requests.
 * Contains the old password for verification and the new password to be set.
 *
 * @param oldPassword the current password of the user, must not be blank
 * @param newPassword the new password to update, must not be blank
 */
public record ChangePasswordRequestDTO(
        @NotBlank(message = "Field 'oldPassword' is required")
        String oldPassword,

        @NotBlank(message = "Field 'newPassword' is required")
        String newPassword
) {
}
