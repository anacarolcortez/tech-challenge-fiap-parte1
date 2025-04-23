package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.UserMapper;
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

    public UsersService(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    public UsersResponseDTO findById(UUID id) {
        Users users = usersRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found.", id)));
        return userMapper.toResponseDTO(users);
    }

    public List<UsersResponseDTO> findAllUsers(int size, int page) {
        var offset = (page - 1) * size;
        var listUsers = usersRepository.findAll(size, offset);
        return userMapper.toResponseDTO(listUsers);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public void createUser(UsersRequestDTO dto) {
        if (isBlank(dto.name()) || isBlank(dto.email()) || isBlank(dto.login())
                || isBlank(dto.password()) || isBlank(dto.role())) {
            throw new IllegalArgumentException("Missing or empty required fields: name, email, login, password, role");
        }

        Role role;
        try {
            role = Role.valueOf(dto.role());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role value. Accepted values: CLIENT, ADMIN, etc.");
        }

        Users user = new Users(
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password(),
                role);

        usersRepository.save(user);
    }

    public UsersResponseDTO updateUser(UUID id, UsersRequestDTO dto) {
        if (isBlank(dto.name()) || isBlank(dto.email()) || isBlank(dto.login())
                || isBlank(dto.password()) || isBlank(dto.role())) {
            throw new IllegalArgumentException("Missing or empty required fields: name, email, login, password, role");
        }

        usersRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found.".formatted(id)));

        Users updatedUser = new Users(
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password(),
                Role.valueOf(dto.role()));

        usersRepository.update(id, updatedUser);
        return userMapper.toResponseDTO(updatedUser);
    }

}
