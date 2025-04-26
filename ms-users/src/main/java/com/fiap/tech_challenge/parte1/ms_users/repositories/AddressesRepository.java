package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AddressesRepository {
    List<Address> findAllByUserId(UUID userId);
    void save(@NotEmpty List<AddressRequestDTO> address, UUID generatedUserId);

    List<Address> findAllByUserIds(Set<UUID> userIdSet);
}
