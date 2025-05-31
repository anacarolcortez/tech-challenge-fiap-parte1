package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.DuplicatedAddressException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validator that checks for duplicate addresses in a user's request.
 * <p>
 * Implements the {@link UserValidator} interface and ensures that all addresses
 * in a user request are unique by comparing all relevant address fields.
 */
@Component
public class DuplicatedAddressValidator implements UserValidator {

    /**
     * Validates the list of addresses in the given {@link UsersRequestDTO} to ensure no duplicates exist.
     *
     * @param dto the DTO containing user data, including the list of addresses.
     * @throws DuplicatedAddressException if duplicate addresses are found.
     */
    @Override
    public void validate(UsersRequestDTO dto) {
        List<AddressRequestDTO> addresses = dto.address();
        validateAddress(addresses);
    }

    /**
     * Checks a list of addresses for duplicates.
     *
     * @param addresses the list of addresses to validate.
     * @throws DuplicatedAddressException if any duplicate addresses are detected.
     */
    public void validateAddress(List<AddressRequestDTO> addresses) {
        Set<String> uniqueAddressKeys = new HashSet<>();
        for (AddressRequestDTO address : addresses) {
            String key = generateAddressKey(address);

            if (!uniqueAddressKeys.add(key)) {
                throw new DuplicatedAddressException(
                        "Duplicate address detected: one or more addresses contain exactly the same data."
                );
            }
        }
    }

    /**
     * Generates a unique key for an address by concatenating its normalized fields.
     *
     * @param address the address object to generate the key for.
     * @return a string key representing the address.
     */
    public String generateAddressKey(AddressRequestDTO address) {
        return String.join("|",
                normalize(address.zipcode()),
                normalize(address.street()),
                normalize(address.number() != null ? address.number().toString() : null),
                normalize(address.complement()),
                normalize(address.neighborhood()),
                normalize(address.city()),
                normalize(address.state())
        );
    }

    /**
     * Normalizes a string value by trimming whitespace and converting to lowercase.
     * Returns an empty string if the value is null.
     *
     * @param value the string to normalize.
     * @return the normalized string or an empty string if null.
     */
    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
