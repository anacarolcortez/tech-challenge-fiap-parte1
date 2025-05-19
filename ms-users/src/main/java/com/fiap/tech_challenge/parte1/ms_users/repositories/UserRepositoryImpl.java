package com.fiap.tech_challenge.parte1.ms_users.repositories;

import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<User> findById(UUID id) {
        return jdbcClient.sql("""
                        SELECT * FROM users
                        WHERE id = :id
                        """)
                .param("id", id)
                .query(User.class)
                .optional();
    }

    @Override
    public List<User> findAll(int size, int offset) {
        return jdbcClient.sql("""
                         SELECT * FROM users LIMIT :size OFFSET :offset
                        """)
                .param("size", size)
                .param("offset", offset)
                .query(User.class)
                .list();
    }

    public UUID save(User user) {

        UUID id = UUID.randomUUID();

        jdbcClient.sql("""
                        INSERT INTO users
                            (id, name, email, login, password, last_modified_date, active, role)
                        VALUES
                            (:id, :name, :email, :login, :password, :last_modified_date, :active, CAST(:role AS role_type));
                        """)
                .param("id", id)
                .param("name", user.getName())
                .param("email", user.getEmail())
                .param("login", user.getLogin())
                .param("password", user.getPassword())
                .param("last_modified_date", user.getDateLastChange())
                .param("active", user.getActive())
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

    @Override
    public void deactivate(UUID id) {
        jdbcClient.sql("UPDATE users SET active = :active, last_modified_date = :last_modified_date WHERE id = :id")
                .param("id", id)
                .param("active", false)
                .param("last_modified_date", new java.sql.Timestamp(new java.util.Date().getTime()))
                .update();
    }

    @Override
    public void reactivate(UUID id) {
        jdbcClient.sql("UPDATE users SET active = :active, last_modified_date = :last_modified_date WHERE id = :id")
                .param("id", id)
                .param("active", true)
                .param("last_modified_date", new java.sql.Timestamp(new java.util.Date().getTime()))
                .update();
    }

    @Override
    public void changePassword(UUID id, String password) {
        jdbcClient.sql("UPDATE users SET password = :password, last_modified_date = :last_modified_date WHERE id = :id")
                .param("id", id)
                .param("password", password)
                .param("last_modified_date", new java.sql.Timestamp(new java.util.Date().getTime()))
                .update();
    }

}
