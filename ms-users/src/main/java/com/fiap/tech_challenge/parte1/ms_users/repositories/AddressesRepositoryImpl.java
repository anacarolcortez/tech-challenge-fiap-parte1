package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Implementation of the AddressesRepository interface using JdbcClient to perform database operations.
 * This repository handles CRUD operations related to Address entities tied to users.
 */
@Repository
public class AddressesRepositoryImpl implements AddressesRepository {

    private final JdbcClient jdbcClient;

    /**
     * Constructs an AddressesRepositoryImpl with the given JdbcClient.
     *
     * @param jdbcClient the JdbcClient used to execute SQL queries
     */
    public AddressesRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    /**
     * Retrieves all addresses associated with the specified user ID.
     *
     * @param userId the UUID of the user whose addresses will be retrieved
     * @return a list of Address entities related to the given userId
     */
    @Override
    public List<Address> findAllByUserId(UUID userId) {
        return jdbcClient.sql("""
                            SELECT
                                id,
                                zipcode,
                                street,
                                number,
                                complement,
                                neighborhood,
                                city,
                                state,
                                user_id
                            FROM
                                address
                            WHERE user_id = :userId
                        """)
                .param("userId", userId)
                .query(Address.class)
                .list();
    }

    /**
     * Saves a list of address DTOs for a given user ID. This operation requires an existing transaction.
     *
     * @param addresses      a non-empty list of AddressRequestDTOs to be saved
     * @param generatedUserId the UUID of the user to associate with these addresses
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(@NotEmpty List<AddressRequestDTO> addresses, UUID generatedUserId) {
        for (AddressRequestDTO address : addresses) {
            jdbcClient.sql("""
                                INSERT INTO address (
                                    user_id, zipcode, street, number, complement, neighborhood, city, state
                                ) VALUES (
                                    :user_id, :zipcode, :street, :number, :complement, :neighborhood, :city, :state
                                );
                            """)
                    .param("user_id", generatedUserId)
                    .param("zipcode", address.zipcode())
                    .param("street", address.street())
                    .param("number", address.number())
                    .param("complement", address.complement())
                    .param("neighborhood", address.neighborhood())
                    .param("city", address.city())
                    .param("state", address.state())
                    .update();
        }
    }

    /**
     * Retrieves all addresses for a given set of user IDs.
     * If the provided set is null or empty, returns an empty list.
     *
     * @param userIdSet a set of UUIDs representing user IDs
     * @return a list of Address entities related to the provided user IDs
     */
    @Override
    public List<Address> findAllByUserIds(Set<UUID> userIdSet) {
        if (userIdSet == null || userIdSet.isEmpty()) {
            return List.of();
        }

        // Generate placeholders like :id0, :id1, ...
        List<String> placeholders = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        int index = 0;
        for (UUID id : userIdSet) {
            String key = "id" + index++;
            placeholders.add(":" + key);
            params.put(key, id);
        }

        String inClause = String.join(", ", placeholders);

        String sql = """
                    SELECT
                        id, zipcode, street, number, complement, neighborhood, city, state, user_id
                    FROM
                        address
                    WHERE
                        user_id IN (%s)
                """.formatted(inClause);

        JdbcClient.StatementSpec spec = jdbcClient.sql(sql);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            spec = spec.param(entry.getKey(), entry.getValue());
        }

        return spec
                .query(Address.class)
                .list();
    }

    /**
     * Deletes all addresses associated with the specified user ID.
     * This operation requires an existing transaction.
     *
     * @param userId the UUID of the user whose addresses will be deleted
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteByUserId(UUID userId) {
        jdbcClient.sql("DELETE FROM address WHERE user_id = :userId")
                .param("userId", userId)
                .update();
    }

}
