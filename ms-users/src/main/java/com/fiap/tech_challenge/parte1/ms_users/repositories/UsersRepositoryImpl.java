package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.Users;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class UsersRepositoryImpl implements UsersRepository {

    private final JdbcClient jdbcClient;

    public UsersRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Users> findById(UUID id) {
        return jdbcClient.sql("""
                        SELECT
                            id, name, email, login, role
                        FROM
                            users
                        WHERE
                            id = :id
                        """)
                .param("id", id)
                .query(Users.class)
                .optional();
    }

    @Override
    public List<Users> findAll(int size, int offset) {
        return jdbcClient.sql("""
                         SELECT * FROM users LIMIT :size OFFSET :offset
                        """)
                .param("size", size)
                .param("offset", offset)
                .query(Users.class)
                .list();
    }

    public UUID save(Users user) {

        UUID id = UUID.randomUUID();

        jdbcClient.sql("""
                        INSERT INTO users
                            (id, name, email, login, password, last_modified_date, status, role)
                        VALUES
                            (:id, :name, :email, :login, :password, :last_modified_date, :status, CAST(:role AS role_type));
                        """)
                .param("id", id)
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("last_modified_date", user.getDateLastChange())
                .param("status", user.getStatus())
                .param("role", user.getRole().name())
                .update();

        return id;
    }

    @Override
    public boolean existsByEmail(String email) {
        Integer count = jdbcClient.sql("""
                        SELECT
                            COUNT(1)
                        FROM
                            users
                        WHERE email = :email
                        """)
                .param("email", email)
                .query(Integer.class)
                .single();

        return count > 0;
    }


    @Override
    public boolean existsByLogin(String login) {
        Integer count = jdbcClient.sql("""
                        SELECT
                            COUNT(1)
                        FROM
                            users
                        WHERE login = :login
                        """)
                .param("login", login)
                .query(Integer.class)
                .single();
        return count > 0;
    }

}
