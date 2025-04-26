package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.mappers.UserMapper;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Users;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.UserNotFoundException;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressesService addressesService;

    @InjectMocks
    private UsersService usersService;

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.findById(id));
    }

    @Test
    void shouldNotThrowUserNotFoundExceptionWhenUserFound() {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(Optional.of(new Users()));
        when(addressesService.findAllByUserId(id)).thenReturn(new ArrayList<>());
        when(userMapper.toResponseDTO(any(Users.class))).thenReturn(new UsersResponseDTO(UUID.fromString("33c652a4-9af9-43a4-8414-f9227de41b38"), "NAME", "EMAIL@EMAIL.COM", "LOGIN", "CLIENT", new ArrayList<>()));
        UsersResponseDTO usersResponseDTO = usersService.findById(id);
        assertEquals("33c652a4-9af9-43a4-8414-f9227de41b38", usersResponseDTO.id().toString());
        assertEquals("NAME", usersResponseDTO.name());
        assertEquals("EMAIL@EMAIL.COM", usersResponseDTO.email());
        assertEquals("LOGIN", usersResponseDTO.login());
        assertTrue(usersResponseDTO.address().isEmpty());
    }

}