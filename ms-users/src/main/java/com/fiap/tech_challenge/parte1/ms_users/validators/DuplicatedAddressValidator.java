package com.fiap.tech_challenge.parte1.ms_users.validators;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.exceptions.DuplicatedAddressException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DuplicatedAddressValidator implements UserValidator {

    @Override
    public void validate(UsersRequestDTO dto) {
        List<AddressRequestDTO> addresses = dto.address();

        Set<String> uniqueAddressKeys = new HashSet<>();

        for (AddressRequestDTO address : addresses) {
            String key = generateAddressKey(address);

            if (!uniqueAddressKeys.add(key)) {
                throw new DuplicatedAddressException(
                        "Endereço duplicado detectado: um ou mais endereços enviados possuem exatamente os mesmos dados."
                );
            }
        }
    }

    private String generateAddressKey(AddressRequestDTO address) {
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

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
