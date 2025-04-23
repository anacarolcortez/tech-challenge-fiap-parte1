package com.fiap.tech_challenge.parte1.ms_users.mappers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Users;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UsersResponseDTO toResponseDTO(Users users) {
        return new UsersResponseDTO(
                users.getId(),
                users.getName(),
                users.getEmail(),
                users.getLogin(),
                users.getRole().name(),
                users.getAddresses());
    }

    public List<UsersResponseDTO> toResponseDTO(List<Users> users) {
        return users.stream().map(this::toResponseDTO).toList();
    }

}
