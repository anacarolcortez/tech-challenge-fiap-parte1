package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import com.fiap.tech_challenge.parte1.ms_users.entities.Role;
import com.fiap.tech_challenge.parte1.ms_users.entities.Users;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.UserNotFoundException;
import com.fiap.tech_challenge.parte1.ms_users.mappers.UserMapper;
import com.fiap.tech_challenge.parte1.ms_users.repositories.UsersRepository;
import com.fiap.tech_challenge.parte1.ms_users.services.validation.UsersValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final AddressesService addressesService;
    private final UsersValidationService usersValidationService;

    public UsersService(UsersRepository usersRepository, UserMapper userMapper, AddressesService addressesService, UsersValidationService usersValidationService) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.addressesService = addressesService;
        this.usersValidationService = usersValidationService;
    }

    public UsersResponseDTO findById(UUID id) {
        Users users = usersRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("Usuário com id %s não encontrado.", id)));
        List<Address> addressList = addressesService.findAllByUserId(id);
        users.setAddress(addressList);
        return userMapper.toResponseDTO(users);
    }

    public List<UsersResponseDTO> findAllUsers(int size, int page) {
        var offset = (page - 1) * size;
        var listUsers = usersRepository.findAll(size, offset);
        Set<UUID> userIdSet = listUsers.stream().map(Users::getId).collect(Collectors.toSet());
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
        Users user = new Users(
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password(),
                Role.valueOf(dto.role())
        );
        return usersRepository.save(user);
    }
}



