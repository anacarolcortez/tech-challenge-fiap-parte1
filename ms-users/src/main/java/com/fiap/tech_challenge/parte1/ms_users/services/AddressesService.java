package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import com.fiap.tech_challenge.parte1.ms_users.repositories.AddressesRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressesService {

    private final AddressesRepository addressesRepository;

    public AddressesService(AddressesRepository addressesRepository) {
        this.addressesRepository = addressesRepository;
    }

    public List<Address> findAllByUserId(UUID userId) {
        return addressesRepository.findAllByUserId(userId);
    }

    public void save(@NotEmpty(message = "User must have at least one Address") List<AddressRequestDTO> address,
            UUID generatedUserId) {
        addressesRepository.save(address, generatedUserId);
    }

    public Map<String, List<Address>> findAllByUserIds(Set<UUID> userIdSet) {
        List<Address> addressList = addressesRepository.findAllByUserIds(userIdSet);
        return addressList.stream()
                .collect(Collectors.groupingBy(Address::getUserId));
    }

    public void update(List<AddressRequestDTO> addressDTOs, UUID userId) {
        addressesRepository.deleteByUserId(userId);
        addressesRepository.save(addressDTOs, userId);
    }
}
