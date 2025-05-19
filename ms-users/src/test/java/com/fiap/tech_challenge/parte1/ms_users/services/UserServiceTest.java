package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.dtos.ChangePasswordRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.InvalidPasswordException;
import com.fiap.tech_challenge.parte1.ms_users.mappers.UserMapper;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.UserNotFoundException;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UserRepository;
import com.fiap.tech_challenge.parte1.ms_users.services.validation.PasswordValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressesService addressesService;

    @InjectMocks
    private UsersService usersService;

    @Mock
    private PasswordValidationService passwordValidationService;

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.findById(id));
    }

    @Test
    void shouldNotThrowUserNotFoundExceptionWhenUserFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User()));
        when(addressesService.findAllByUserId(id)).thenReturn(new ArrayList<>());
        when(userMapper.toResponseDTO(any(User.class))).thenReturn(new UsersResponseDTO(UUID.fromString("33c652a4-9af9-43a4-8414-f9227de41b38"), "NAME", "EMAIL@EMAIL.COM", "LOGIN", "CLIENT", new ArrayList<>()));
        UsersResponseDTO usersResponseDTO = usersService.findById(id);
        assertEquals("33c652a4-9af9-43a4-8414-f9227de41b38", usersResponseDTO.id().toString());
        assertEquals("NAME", usersResponseDTO.name());
        assertEquals("EMAIL@EMAIL.COM", usersResponseDTO.email());
        assertEquals("LOGIN", usersResponseDTO.login());
        assertTrue(usersResponseDTO.address().isEmpty());
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO("oldPass", "newPass");

        when(userRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> usersService.changePassword(id, dto));
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenOldPasswordDoesNotMatch() {
        UUID id = UUID.randomUUID();
        User user = Mockito.mock(User.class);
        String mockedPassword = "correctOldPass";
        when(user.getPassword()).thenReturn(mockedPassword);

        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO("wrongOldPass", "newPass");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doThrow(new InvalidPasswordException("Senha atual não confere"))
                .when(passwordValidationService).validate(dto, mockedPassword);
        assertThrows(InvalidPasswordException.class, () -> usersService.changePassword(id, dto));
    }

    @Test
    void shouldThrowInvalidPasswordExceptionWhenNewPasswordIsSameAsOld() {
        UUID id = UUID.randomUUID();
        User user = Mockito.mock(User.class);
        String mockedPassword = "samePass";
        when(user.getPassword()).thenReturn(mockedPassword);

        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO("samePass", "samePass");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doThrow(new InvalidPasswordException("Nova senha deve ser diferente da senha antiga"))
                .when(passwordValidationService).validate(dto, mockedPassword);
        assertThrows(InvalidPasswordException.class, () -> usersService.changePassword(id, dto));
    }
}
