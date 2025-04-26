package com.fiap.tech_challenge.parte1.ms_users.mappers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddressMapper {

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

    public List<AddressResponseDTO> toAddressRequestDTO(List<Address> addresses) {
        return addresses.stream().map(this::toAddressRequestDTO).toList();
    }

}
