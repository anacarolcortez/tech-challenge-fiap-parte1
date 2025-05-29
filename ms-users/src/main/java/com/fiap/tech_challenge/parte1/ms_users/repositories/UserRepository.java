package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);

    Optional<User> findByLogin(String login);

    List<User> findAll(int size, int offset);

    UUID save(User user);

    boolean existsByEmail(@NotBlank(message = "User field 'email' is required") @Email(message = "User field 'email' must be a valid email address") String email);

    boolean existsByLogin(@NotBlank(message = "User field 'login' is required") String login);

    void deactivate(UUID id);

    void reactivate(UUID id);

    void changePassword(UUID id, String password);
}
