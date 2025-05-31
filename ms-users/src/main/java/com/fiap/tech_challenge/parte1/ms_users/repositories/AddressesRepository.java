package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Repository interface responsible for CRUD operations on Address entities.
 */
public interface AddressesRepository {

    /**
     * Retrieves all addresses associated with a specific user ID.
     *
     * @param userId the UUID of the user whose addresses are to be retrieved
     * @return a list of Address entities belonging to the given user
     */
    List<Address> findAllByUserId(UUID userId);

    /**
     * Saves a list of addresses for a specified user ID.
     *
     * @param address a non-empty list of AddressRequestDTO objects to be saved
     * @param generatedUserId the UUID of the user to whom the addresses belong
     */
    void save(@NotEmpty List<AddressRequestDTO> address, UUID generatedUserId);

    /**
     * Retrieves all addresses associated with a set of user IDs.
     *
     * @param userIdSet a set of UUIDs representing the users
     * @return a list of Address entities belonging to the users in the provided set
     */
    List<Address> findAllByUserIds(Set<UUID> userIdSet);

    /**
     * Deletes all addresses associated with a specific user ID.
     *
     * @param id the UUID of the user whose addresses are to be deleted
     */
    void deleteByUserId(UUID id);

}
