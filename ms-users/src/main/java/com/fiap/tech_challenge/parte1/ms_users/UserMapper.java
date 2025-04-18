package com.fiap.tech_challenge.parte1.ms_users;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UsersResponseDTO toResponseDTO(Users users) {
        return new UsersResponseDTO(users.getId(), users.getName(), users.getEmail(), users.getLogin(), users.getRole().name());
    }

}
