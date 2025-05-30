package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.dtos.ChangePasswordRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UpdateUserDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import com.fiap.tech_challenge.parte1.ms_users.entities.Role;
import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.EmailAlreadyExistsException;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.LoginAlreadyExistsException;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.UserNotFoundException;
import com.fiap.tech_challenge.parte1.ms_users.mappers.UserMapper;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UserRepository;
import com.fiap.tech_challenge.parte1.ms_users.services.validation.PasswordValidationService;
import com.fiap.tech_challenge.parte1.ms_users.services.validation.UsersValidationService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UsersService(UserRepository userRepository, UserMapper userMapper, AddressesService addressesService, UsersValidationService usersValidationService, PasswordValidationService passwordValidationService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.addressesService = addressesService;
        this.usersValidationService = usersValidationService;
        this.passwordValidationService = passwordValidationService;
        this.passwordEncoder = passwordEncoder;
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
    public UsersResponseDTO createUser(UsersRequestDTO dto) {
        UUID generatedUserId = handleUserCreation(dto);
        handleUserRelatedAddressCreation(dto, generatedUserId);
        return findById(generatedUserId);
    }

    private void handleUserRelatedAddressCreation(UsersRequestDTO dto, UUID generatedUserId) {
        if (dto.address() != null && !dto.address().isEmpty()) {
            addressesService.save(dto.address(), generatedUserId);
        }
    }

    private UUID handleUserCreation(UsersRequestDTO dto) {
        usersValidationService.validateAll(dto);

        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = new User(
                dto.name(),
                dto.email(),
                dto.login(),
                encodedPassword,
                Role.valueOf(dto.role())
        );
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

        // Check if old password matches
        boolean oldPasswordMatches = passwordEncoder.matches(dto.oldPassword(), user.getPassword());
        // Check if new password is same as old one (in plaintext)
        boolean isSameAsOld = dto.oldPassword().equals(dto.newPassword());
        passwordValidationService.validate(oldPasswordMatches, isSameAsOld);
        String newPasswordEncoded = passwordEncoder.encode(dto.newPassword());
        userRepository.changePassword(id, newPasswordEncoded);
    }

    @Transactional
    public UsersResponseDTO updateUser(UUID id, UpdateUserDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id %s not found.".formatted(id)));
        userUpdateValidations(dto, existingUser);
        userRepository.update(
                existingUser.getId(),
                dto.name(),
                dto.email(),
                dto.login(),
                existingUser.getPassword());

        if (dto.address() != null && !dto.address().isEmpty()) {
            addressesService.update(dto.address(), existingUser.getId());
        }

        return findById(id);
    }

    private void userUpdateValidations(UpdateUserDTO dto, User existingUser) {
        if (userRepository.emailAlreadyExistsForDifferentUsers(dto.email(), existingUser.getId())) {
            throw new EmailAlreadyExistsException("O e-mail informado já está em uso.");
        }

        if (userRepository.loginAlreadyExistsForDifferentUsers(dto.login(), existingUser.getId())) {
            throw new LoginAlreadyExistsException("O login informado já está em uso.");
        }
    }
}
