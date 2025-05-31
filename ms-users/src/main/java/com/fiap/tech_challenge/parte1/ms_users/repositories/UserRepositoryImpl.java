package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the {@link UserRepository} interface using Spring's {@link JdbcClient} for data access.
 * Handles CRUD operations and queries related to User entities in the database.
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcClient jdbcClient;

    /**
     * Constructs a new UserRepositoryImpl with the provided {@link JdbcClient}.
     *
     * @param jdbcClient the JdbcClient used to execute SQL queries
     */
    public UserRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    /**
     * Finds a user by their unique identifier.
     *
     * @param id the UUID of the user to find
     * @return an Optional containing the User if found, or empty if no user with the given ID exists
     */
    public Optional<User> findById(UUID id) {
        return jdbcClient.sql("""
                        SELECT * FROM users
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(User.class)
                .optional();
    }

    /**
     * Checks if an email already exists in the database for a different user than the one specified.
     *
     * @param email the email to check for existence
     * @param id the UUID of the user to exclude from the check
     * @return true if another user with the email exists, false otherwise
     */
    @Override
    public boolean emailAlreadyExistsForDifferentUsers(String email, UUID id) {
        return jdbcClient.sql("""
                            SELECT 1 FROM users
                            WHERE email = :email AND id <> :id
                            LIMIT 1
                        """)
                .param("email", email)
                .param("id", id)
                .query()
                .optionalValue()
                .isPresent();
    }

    /**
     * Checks if a login already exists in the database for a different user than the one specified.
     *
     * @param login the login to check for existence
     * @param id the UUID of the user to exclude from the check
     * @return true if another user with the login exists, false otherwise
     */
    @Override
    public boolean loginAlreadyExistsForDifferentUsers(String login, UUID id) {
        return jdbcClient.sql("""
                            SELECT 1 FROM users
                            WHERE login = :login AND id <> :id
                            LIMIT 1
                        """)
                .param("login", login)
                .param("id", id)
                .query()
                .optionalValue()
                .isPresent();
    }

    /**
     * Finds a user by their login.
     *
     * @param login the login string to search by
     * @return an Optional containing the User if found, or empty if no user with the login exists
     */
    @Override
    public Optional<User> findByLogin(String login) {
        return jdbcClient.sql("""
                        SELECT * FROM users
                        WHERE login = :login
                        """)
                .param("login", login)
                .query(User.class)
                .optional();
    }

    /**
     * Retrieves a paginated list of users from the database.
     *
     * @param size the maximum number of users to return
     * @param offset the number of users to skip (offset) for pagination
     * @return a list of User entities
     */
    @Override
    public List<User> findAll(int size, int offset) {
        return jdbcClient.sql("""
                         SELECT * FROM users LIMIT :size OFFSET :offset
                        """)
                .param("size", size)
                .param("offset", offset)
                .query(User.class)
                .list();
    }

    /**
     * Saves a new user entity in the database.
     *
     * @param user the User entity to save
     * @return the UUID assigned to the newly saved user
     */
    public UUID save(User user) {

        UUID id = UUID.randomUUID();

        jdbcClient.sql("""
                        INSERT INTO users
                            (id, name, email, login, password, last_modified_date, active, role)
                        VALUES
                            (:id, :name, :email, :login, :password, :last_modified_date, :active, CAST(:role AS role_type));
                        """)
                .param("id", id)
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("last_modified_date", user.getDateLastChange())
                .param("active", user.getActive())
                .param("role", user.getRole().name())
                .update();

        return id;
    }

    /**
     * Checks if any user exists with the specified email.
     *
     * @param email the email to check for existence
     * @return true if a user with the email exists, false otherwise
     */
    @Override
    public boolean existsByEmail(String email) {
        Integer count = jdbcClient.sql("""
                        SELECT
                            COUNT(1)
                        FROM
                            users
                        WHERE email = :email
                        """)
                .param("email", email)
                .query(Integer.class)
                .single();

        return count > 0;
    }

    /**
     * Checks if any user exists with the specified login.
     *
     * @param login the login to check for existence
     * @return true if a user with the login exists, false otherwise
     */
    @Override
    public boolean existsByLogin(String login) {
        Integer count = jdbcClient.sql("""
                        SELECT
                            COUNT(1)
                        FROM
                            users
                        WHERE login = :login
                        """)
                .param("login", login)
                .query(Integer.class)
                .single();
        return count > 0;
    }

    /**
     * Deactivates a user account by setting the active flag to false and updating the last modified date.
     *
     * @param id the UUID of the user to deactivate
     */
    @Override
    public void deactivate(UUID id) {
        jdbcClient.sql("UPDATE users SET active = :active, last_modified_date = :last_modified_date WHERE id = :id")
                .param("id", id)
                .param("active", false)
                .param("last_modified_date", now())
                .update();
    }

    /**
     * Reactivates a user account by setting the active flag to true and updating the last modified date.
     *
     * @param id the UUID of the user to reactivate
     */
    @Override
    public void reactivate(UUID id) {
        jdbcClient.sql("UPDATE users SET active = :active, last_modified_date = :last_modified_date WHERE id = :id")
                .param("id", id)
                .param("active", true)
                .param("last_modified_date", now())
                .update();
    }

    /**
     * Changes the password of the specified user and updates the last modified date.
     *
     * @param id the UUID of the user whose password is to be changed
     * @param password the new password (expected to be already hashed)
     */
    @Override
    public void changePassword(UUID id, String password) {
        jdbcClient.sql("UPDATE users SET password = :password, last_modified_date = :last_modified_date WHERE id = :id")
                .param("id", id)
                .param("password", password)
                .param("last_modified_date", now())
                .update();
    }

    /**
     * Updates the basic information of an existing user, including name, email, login, and password.
     *
     * @param id the UUID of the user to update
     * @param name the new name for the user
     * @param email the new email for the user
     * @param login the new login for the user
     * @param password the new password (expected to be hashed)
     */
    @Override
    public void update(UUID id, String name, String email, String login, String password) {
        jdbcClient.sql("""
                            UPDATE users
                            SET name = :name,
                                email = :email,
                                login = :login,
                                password = :password,
                                last_modified_date = :last_modified_date
                            WHERE id = :id
                        """)
                .param("id", id)
                .param("name", name)
                .param("email", email)
                .param("login", login)
                .param("password", password)
                .param("last_modified_date", new Timestamp(System.currentTimeMillis()))
                .update();
    }

    /**
     * Returns the current timestamp to be used as last modified date in the database.
     *
     * @return the current timestamp
     */
    private Timestamp now() {
        return Timestamp.from(Instant.now());
    }

}
