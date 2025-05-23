package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UpdateUserDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.ChangePasswordRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import com.fiap.tech_challenge.parte1.ms_users.entities.Role;
import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.InvalidPasswordException;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.UserNotFoundException;
import com.fiap.tech_challenge.parte1.ms_users.mappers.UserMapper;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UserRepository;
import com.fiap.tech_challenge.parte1.ms_users.services.validation.PasswordValidationService;
import com.fiap.tech_challenge.parte1.ms_users.services.validation.UsersValidationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressesService addressesService;
    private final UsersValidationService usersValidationService;
    private final PasswordValidationService passwordValidationService;

    public UsersService(UserRepository userRepository, UserMapper userMapper, AddressesService addressesService,
            UsersValidationService usersValidationService, PasswordValidationService passwordValidationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressesService = addressesService;
        this.usersValidationService = usersValidationService;
        this.passwordValidationService = passwordValidationService;
    }

    public UsersResponseDTO findById(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Usuário com id %s não encontrado.", id)));
        List<Address> addressList = addressesService.findAllByUserId(id);
        user.setAddress(addressList);
        return userMapper.toResponseDTO(user);
    }

    public List<UsersResponseDTO> findAllUsers(int size, int page) {
        var offset = (page - 1) * size;
        var listUsers = userRepository.findAll(size, offset);
        Set<UUID> userIdSet = listUsers.stream().map(User::getId).collect(Collectors.toSet());
        Map<String, List<Address>> addressByUserMap = addressesService.findAllByUserIds(userIdSet);

        listUsers.forEach(user -> user.setAddress(addressByUserMap.getOrDefault(user.getId().toString(), List.of())));

        return userMapper.toResponseDTO(listUsers);
    }

    @Transactional
    public void createUser(UsersRequestDTO dto) {
        UUID generatedUserId = handleUserCreation(dto);
        handleUserRelatedAddressCreation(dto, generatedUserId);
    }

    private void handleUserRelatedAddressCreation(UsersRequestDTO dto, UUID generatedUserId) {
        if (dto.address() != null && !dto.address().isEmpty()) {
            addressesService.save(dto.address(), generatedUserId);
        }
    }

    private UUID handleUserCreation(UsersRequestDTO dto) {
        usersValidationService.validateAll(dto);
        User user = new User(
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password(),
                Role.valueOf(dto.role()));
        return userRepository.save(user);
    }

    public void deactivateUser(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Usuário com id %s não encontrado.", id)));
        userRepository.deactivate(user.getId());
    }

    public void reactivateUser(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Usuário com id %s não encontrado.", id)));
        userRepository.reactivate(user.getId());
    }

    public void changePassword(UUID id, @Valid ChangePasswordRequestDTO dto) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Usuário com id %s não encontrado.", id)));
        passwordValidationService.validate(dto, user.getPassword());
        userRepository.changePassword(id, dto.newPassword());
    }

    @Transactional
    public UsersResponseDTO updateUser(UUID id, UpdateUserDTO dto) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found.".formatted(id)));

        userRepository.update(
                existingUser.getId(),
                dto.name(),
                dto.email(),
                dto.login(),
                existingUser.getPassword());

        if (dto.address() != null && !dto.address().isEmpty()) {
            addressesService.update(dto.address(), existingUser.getId());
        }

        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found.".formatted(id)));

        List<Address> updatedAddresses = addressesService.findAllByUserId(id);
        updatedUser.setAddress(updatedAddresses);

        return userMapper.toResponseDTO(updatedUser);
    }
}
