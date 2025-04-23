package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.Address;

import java.util.List;
import java.util.UUID;

public interface AddressesRepository {
    List<Address> findAllByUserId(UUID userId);
}
