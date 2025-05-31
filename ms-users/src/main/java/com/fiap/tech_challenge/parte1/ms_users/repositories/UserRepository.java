package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing User entities.
 * Provides methods for querying, saving, updating, and managing user records.
 */
public interface UserRepository {

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the UUID of the user to find
     * @return an Optional containing the User if found, or empty if no user with the given ID exists
     */
    Optional<User> findById(UUID id);

    /**
     * Checks if an email is already associated with a different user than the one specified.
     * Useful for email uniqueness validation during updates.
     *
     * @param email the email address to check
     * @param uuid the UUID of the user to exclude from the check (usually the current user)
     * @return true if the email exists for a different user, false otherwise
     */
    boolean emailAlreadyExistsForDifferentUsers(@Email String email, @org.hibernate.validator.constraints.UUID UUID uuid);

    /**
     * Checks if a login is already associated with a different user than the one specified.
     * Useful for login uniqueness validation during updates.
     *
     * @param login the login (username/email) to check
     * @param uuid the UUID of the user to exclude from the check
     * @return true if the login exists for a different user, false otherwise
     */
    boolean loginAlreadyExistsForDifferentUsers(@Email String login, @org.hibernate.validator.constraints.UUID UUID uuid);

    /**
     * Finds a user by their login (username or email).
     *
     * @param login the login string to search by
     * @return an Optional containing the User if found, or empty if no user with the given login exists
     */
    Optional<User> findByLogin(String login);

    /**
     * Retrieves a paginated list of users.
     *
     * @param size the maximum number of users to return
     * @param offset the offset from the start of the user list (used for pagination)
     * @return a list of User entities
     */
    List<User> findAll(int size, int offset);

    /**
     * Saves a new user record to the data store.
     *
     * @param user the User entity to save
     * @return the UUID assigned to the saved user
     */
    UUID save(User user);

    /**
     * Updates an existing user's basic information.
     *
     * @param id the UUID of the user to update
     * @param name the new name for the user
     * @param email the new email for the user
     * @param login the new login for the user
     * @param password the new password for the user (should be hashed)
     */
    void update(UUID id, String name, String email, String login, String password);

    /**
     * Checks if a user exists with the given email.
     *
     * @param email the email address to check; must not be blank and must be valid
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(
            @NotBlank(message = "User field 'email' is required")
            @Email(message = "User field 'email' must be a valid email address") String email);

    /**
     * Checks if a user exists with the given login.
     *
     * @param login the login string to check; must not be blank
     * @return true if a user with the login exists, false otherwise
     */
    boolean existsByLogin(@NotBlank(message = "User field 'login' is required") String login);

    /**
     * Deactivates the user account identified by the given UUID.
     * Typically, sets an 'active' flag to false or similar.
     *
     * @param id the UUID of the user to deactivate
     */
    void deactivate(UUID id);

    /**
     * Reactivates the user account identified by the given UUID.
     * Typically, sets an 'active' flag to true or similar.
     *
     * @param id the UUID of the user to reactivate
     */
    void reactivate(UUID id);

    /**
     * Changes the password for the specified user.
     *
     * @param id the UUID of the user whose password will be changed
     * @param password the new password (should be hashed before saving)
     */
    void changePassword(UUID id, String password);

}
