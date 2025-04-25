package com.fiap.tech_challenge.parte1.ms_users.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(
        @NotBlank(message = "Address field 'zipcode' is required")
        String zipcode,

        @NotBlank(message = "Address field 'street' is required")
        String street,

        @NotNull(message = "Address field 'number' is required")
        Integer number,

        String complement, // optional

        @NotBlank(message = "Address field 'neighborhood' is required")
        String neighborhood,

        @NotBlank(message = "Address field 'city' is required")
        String city,

        @NotBlank(message = "Address field 'state' is required")
        @Size(min = 2, max = 2, message = "Address field 'state' must be a 2-letter code")
        String state
) {
}
