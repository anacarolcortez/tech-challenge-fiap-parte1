package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.Users;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository {

    Optional<Users> findById(UUID id);

    void save(Users user);

}
