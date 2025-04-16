package com.fiap.tech_challenge.parte1.ms_users.repositories;
import com.fiap.tech_challenge.parte1.ms_users.entities.Users;


import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;


@Repository
public class UsersRepository {

    private final JdbcClient jdbcClient;

    public UsersRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void save(Users user) {
        jdbcClient.sql("""
                INSERT INTO users (name, email, login, password, date_last_change, status, role)
                VALUES (:name, :email, :login, :password, :dateLastChange, :status, :role)
                """)
            .param("name", user.getName())
            .param("email", user.getEmail())
            .param("login", user.getLogin())
            .param("password", user.getPassword())
            .param("dateLastChange", user.getDateLastChange())
            .param("status", user.getStatus())
            .param("role", user.getRole().name())
            .update();
    }
}
