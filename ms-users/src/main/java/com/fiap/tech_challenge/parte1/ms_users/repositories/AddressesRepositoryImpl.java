package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AddressRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.Address;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Override
    @Transactional
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

    @Override
    public List<Address> findAllByUserIds(Set<UUID> userIdSet) {
        if (userIdSet == null || userIdSet.isEmpty()) {
            return List.of();
        }

        // Gera a lista de placeholders :id0, :id1, ...
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


}
