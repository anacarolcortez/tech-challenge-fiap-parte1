package com.fiap.tech_challenge.parte1.ms_users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for address requests.
 * This DTO is used to transfer address data from client to server
 * when creating or updating a user's address.
 * Validation constraints ensure required fields are present and valid.
 *
 * @param zipcode      the postal code of the address (required, non-blank)
 * @param street       the street name of the address (required, non-blank)
 * @param number       the street number (required, not null)
 * @param complement   an optional additional address detail
 * @param neighborhood the neighborhood name (required, non-blank)
 * @param city         the city name (required, non-blank)
 * @param state        the two-letter state code (required, exactly 2 characters)
 */
public record AddressRequestDTO(
        @NotBlank(message = "Address field 'zipcode' is required")
        String zipcode,

        @NotBlank(message = "Address field 'street' is required")
        String street,

        @NotNull(message = "Address field 'number' is required")
        Integer number,

        String complement,

        @NotBlank(message = "Address field 'neighborhood' is required")
        String neighborhood,

        @NotBlank(message = "Address field 'city' is required")
        String city,

        @NotBlank(message = "Address field 'state' is required")
        @Size(min = 2, max = 2, message = "Address field 'state' must be a 2-letter code")
        String state
) {
}
