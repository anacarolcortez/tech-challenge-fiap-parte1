package com.fiap.tech_challenge.parte1.ms_users.validators;

/**
 * Interface for validating password update rules.
 * <p>
 * Implementations should define the logic to validate password changes,
 * such as checking if the old password matches and whether the new password is different.
 */
public interface PasswordValidator {

    /**
     * Validates password change conditions.
     *
     * @param oldPasswordMatches indicates whether the provided old password matches the stored one.
     * @param isSameAsOld        indicates whether the new password is the same as the old one.
     * @throws RuntimeException or a custom exception if the validation fails.
     */
    void validate(boolean oldPasswordMatches, boolean isSameAsOld);
}
