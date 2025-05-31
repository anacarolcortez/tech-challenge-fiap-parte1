package com.fiap.tech_challenge.parte1.ms_users.services;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import com.fiap.tech_challenge.parte1.ms_users.repositories.AddressesRepository;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service responsible for managing user addresses,
 * including retrieval, saving, updating, and grouping addresses by user.
 */
@Service
public class AddressesService {

    private final AddressesRepository addressesRepository;

    /**
     * Constructs the AddressesService with the given repository.
     *
     * @param addressesRepository repository used for address persistence operations
     */
    public AddressesService(AddressesRepository addressesRepository) {
        this.addressesRepository = addressesRepository;
    }

    /**
     * Retrieves all addresses associated with a specific user by their UUID.
     *
     * @param userId the UUID of the user whose addresses are to be fetched
     * @return a list of addresses belonging to the specified user
     */
    public List<Address> findAllByUserId(UUID userId) {
        return addressesRepository.findAllByUserId(userId);
    }

    /**
     * Saves a list of address DTOs for a given user ID.
     * Requires the address list to be non-empty.
     *
     * @param address         the list of address DTOs to save
     * @param generatedUserId the UUID of the user to whom these addresses belong
     * @throws jakarta.validation.ConstraintViolationException if the address list is empty
     */
    public void save(@NotEmpty(message = "User must have at least one Address") List<AddressRequestDTO> address,
                     UUID generatedUserId) {
        addressesRepository.save(address, generatedUserId);
    }

    /**
     * Retrieves addresses for a set of user IDs and groups them by user ID (as String).
     *
     * @param userIdSet the set of user UUIDs to fetch addresses for
     * @return a map where the key is the user ID (String) and the value is the list of addresses for that user
     */
    public Map<String, List<Address>> findAllByUserIds(Set<UUID> userIdSet) {
        List<Address> addressList = addressesRepository.findAllByUserIds(userIdSet);
        return addressList.stream()
                .collect(Collectors.groupingBy(Address::getUserId));
    }

    /**
     * Updates the addresses of a user by deleting existing addresses and saving the new list.
     *
     * @param addressDTOs the new list of address DTOs to be saved
     * @param userId      the UUID of the user whose addresses are to be updated
     */
    public void update(List<AddressRequestDTO> addressDTOs, UUID userId) {
        addressesRepository.deleteByUserId(userId);
        addressesRepository.save(addressDTOs, userId);
    }
}
