package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.Users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository {

    Optional<Users> findById(UUID id);
    List<Users> findAll(int size, int offset);
    void save(Users user);
}
