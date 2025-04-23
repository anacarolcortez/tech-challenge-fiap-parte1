package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class AddressesRepositoryImpl implements AddressesRepository {

    private final JdbcClient jdbcClient;

    public AddressesRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Address> findAllByUserId(UUID userId) {
        return jdbcClient.sql("""
                            SELECT
                                a.id,
                                a.zipcode,
                                a.street,
                                a.number,
                                a.complement,
                                a.neighborhood,
                                a.city,
                                a.state
                            FROM
                                address a
                            INNER JOIN user_addresses ua ON a.id = ua.address_id
                            WHERE ua.user_id = :userId
                        """)
                .param("userId", userId)
                .query(Address.class)
                .list();
    }
}
