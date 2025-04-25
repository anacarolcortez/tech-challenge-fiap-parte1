package com.fiap.tech_challenge.parte1.ms_users.dtos;

import java.util.UUID;

public record AddressResponseDTO(
        UUID id,
        String zipcode,
        String street,
        Integer number,
        String complement, // optional
        String neighborhood,
        String city,
        String state,
        String userId
) {
}
