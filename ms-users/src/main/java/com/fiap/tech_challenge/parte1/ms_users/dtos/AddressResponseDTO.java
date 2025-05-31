package com.fiap.tech_challenge.parte1.ms_users.dtos;

import java.util.UUID;

/**
 * Data Transfer Object for address responses.
 * This DTO is used to send address data from the server to the client,
 * typically after creating or retrieving an address.
 *
 * @param id           unique identifier of the address
 * @param zipcode      postal code of the address
 * @param street       street name
 * @param number       street number
 * @param complement   optional additional address details
 * @param neighborhood neighborhood name
 * @param city         city name
 * @param state        two-letter state code
 * @param userId       identifier of the user owning the address
 */
public record AddressResponseDTO(
        UUID id,
        String zipcode,
        String street,
        Integer number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String userId
) {
}
