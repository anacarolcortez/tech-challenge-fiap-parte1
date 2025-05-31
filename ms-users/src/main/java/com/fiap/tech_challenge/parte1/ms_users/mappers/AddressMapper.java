package com.fiap.tech_challenge.parte1.ms_users.mappers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper component responsible for converting {@link Address} entities
 * to {@link AddressResponseDTO} data transfer objects.
 */
@Component
public class AddressMapper {

    /**
     * Converts a single {@link Address} entity to an {@link AddressResponseDTO}.
     *
     * @param address the {@link Address} entity to convert
     * @return the corresponding {@link AddressResponseDTO}
     */
    public AddressResponseDTO toAddressRequestDTO(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getZipcode(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getUserId());
    }

    /**
     * Converts a list of {@link Address} entities to a list of {@link AddressResponseDTO}s.
     *
     * @param addresses the list of {@link Address} entities to convert
     * @return a list of corresponding {@link AddressResponseDTO}s
     */
    public List<AddressResponseDTO> toAddressRequestDTO(List<Address> addresses) {
        return addresses.stream()
                .map(this::toAddressRequestDTO)
                .toList();
    }

}
