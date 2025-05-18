package com.fiap.tech_challenge.parte1.ms_users.mappers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    private final AddressMapper addressMapper;

    public UserMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public UsersResponseDTO toResponseDTO(User user) {
        return new UsersResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getRole().name(),
                addressMapper.toAddressRequestDTO(user.getAddresses()));
    }

    public List<UsersResponseDTO> toResponseDTO(List<User> users) {
        return users.stream().map(this::toResponseDTO).toList();
    }

}
