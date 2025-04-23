package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import com.fiap.tech_challenge.parte1.ms_users.mappers.UserMapper;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Role;
import com.fiap.tech_challenge.parte1.ms_users.entities.Users;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.UserNotFoundException;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UsersRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.UUID;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final AddressesService addressesService;

    public UsersService(UsersRepository usersRepository, UserMapper userMapper, AddressesService addressesService) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.addressesService = addressesService;
    }

    public UsersResponseDTO findById(UUID id) {
        Users users = usersRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found.", id)));
        List<Address> addressList = addressesService.findAllByUserId(id);
        users.setAddress(addressList);
        return userMapper.toResponseDTO(users);
    }

    public List<UsersResponseDTO> findAllUsers(int size, int page){
        var offset = (page - 1) * size;
        var listUsers = usersRepository.findAll(size, offset);
        return  userMapper.toResponseDTO(listUsers);
    }

    public void createUser(UsersRequestDTO dto) {
        Users user = new Users(
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password(),
                Role.valueOf(dto.role())
        );
        usersRepository.save(user);
    }
}



