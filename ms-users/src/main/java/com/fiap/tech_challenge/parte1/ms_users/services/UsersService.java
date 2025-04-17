package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Role;
import com.fiap.tech_challenge.parte1.ms_users.entities.Users;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void createUser(UsersRequestDTO dto) {
        Users user = new Users(
                dto.name,
                dto.email,
                dto.login,
                dto.password,
                new Date(),                    // data atual
                new ArrayList<>(),             // lista de endereços vazia
                Role.valueOf(dto.role)
        );

        usersRepository.save(user);
    }
}



